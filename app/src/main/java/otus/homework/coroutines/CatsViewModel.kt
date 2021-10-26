package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsServiceImg: CatsServiceImg)
    : ViewModel() {

    val catsData: MutableLiveData<Result> by lazy {
        MutableLiveData<Result>()
    }

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.logThrowable(throwable)
        }) {
            try {
                val response =
                    async(Dispatchers.Default) {
                        catsService.getCatFact()
                    }

                val responseImg = async(Dispatchers.Default) {
                    catsServiceImg.getCatImage()
                }

                val resFact = response.await()
                val resImage = responseImg.await()

                withContext(Dispatchers.Main) {
                    if (checkResponse(resFact)) {
                        if (checkResponse(resImage)) {
                            catsData.value = Success(CatsData(
                                resFact.body()!!.text,
                                resImage.body()!!.file
                            ))
                        } else {
                            CrashMonitor.trackWarning()
                        }
                    } else {
                        CrashMonitor.trackWarning()
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> catsData.value = NetworkError
                    else -> {
                        CrashMonitor.logException(e)
                        e.message?.let {
                            catsData.value = Error(it)
                        }
                    }
                }
            }
        }
    }

    private fun checkResponse(response: Response<*>): Boolean {
        return response.isSuccessful && response.body() != null
    }
}