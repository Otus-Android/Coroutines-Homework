package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException


class CatsViewModel : ViewModel() {

    private val diContainer: DiContainer = DiContainer()
    private val catsService: CatsService = diContainer.service

    val livePresentationModel: LiveData<Result>
        get() = _livePresentationModel

    private val _livePresentationModel by lazy {
        MutableLiveData<Result>()
    }

    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(throwable as Exception)
        }

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {

            var catImageUrl: String? = null
            var catFactText = "Something went wrong :("

            withContext(exceptionHandler) {
                val imageJob = launch {
                    try {
                        val response = catsService.getCatImageUrl()
                        if (response.isSuccessful && response.body() != null) {
                            catImageUrl = response.body()!!.fileUrl
                        }
                    } catch (e: Exception) {
                        handleException(e)
                    }
                }

                val factJob = launch {
                    try {
                        val response = catsService.getCatFact()
                        if (response.isSuccessful && response.body() != null) {
                            catFactText = response.body()!!.text
                        }
                    } catch (e: Exception) {
                        handleException(e)
                    }
                }

                imageJob.join()
                factJob.join()

            }

            if (catImageUrl != null) {
                val catPresentationModel = CatPresentationModel(
                    catFactMessage = catFactText,
                    catImageUrl = catImageUrl!!
                )

                _livePresentationModel.value =
                    Result.Success<CatPresentationModel>().apply {
                        successBody = catPresentationModel
                    }
            }
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is CancellationException -> throw e
            is SocketTimeoutException ->
                _livePresentationModel.value = Result.Error(
                    message = "Server do not response"
                )
            else -> {
                CrashMonitor.trackWarning(e)
                _livePresentationModel.value = Result.Error(
                    message = e.message!!
                )
            }
        }
    }
}