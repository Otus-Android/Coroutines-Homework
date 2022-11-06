package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    val livePresentationModel: LiveData<Result>
        get() = _livePresentationModel

    private val _livePresentationModel = MutableLiveData<Result>()
    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(throwable as Exception)
        }

    fun fetchData() {

        val catImageUrlDeferred = viewModelScope.async { catsService.getCatImageUrl() }
        val catFactDeferred = viewModelScope.async { catsService.getCatFact() }

        viewModelScope.launch(exceptionHandler) {
            try {
                val catImageUrlResponse = catImageUrlDeferred.await()
                val catFactResponse = catFactDeferred.await()
                if (catImageUrlResponse.isSuccessful && catImageUrlResponse.body() != null &&
                    catFactResponse.isSuccessful && catFactResponse.body() != null ) {
                    val catPresentationModel = CatPresentationModel(
                        catFactMessage = catFactResponse.body()!!.fact,
                        catImageUrl = catImageUrlResponse.body()!!.fileUrl
                    )

                    _livePresentationModel.value =
                        Result.Success<CatPresentationModel>().apply {
                            successBody = catPresentationModel
                        }
                }
            } catch (e: Exception) {
                handleException(e)
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