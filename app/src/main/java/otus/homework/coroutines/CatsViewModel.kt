package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {
    private var _catsLiveData = MutableLiveData<Result>()
    val catsLiveData: LiveData<Result> = _catsLiveData
    val diConteiner = DiContainer()
    val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    companion object {
        private const val TAG = "CatsViewModel"
    }

    val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, e ->
            (when (e) {
                is SocketTimeoutException -> {
                    _catsLiveData.value =
                        Result.Error(e, e.message ?: " SocketTimeoutException")
                    CrashMonitor.trackWarning(TAG, e)
                }
                else -> {
                    _catsLiveData.value =
                        Result.Error(e, "Не удалось получить ответ от сервера")
                    CrashMonitor.trackWarning(TAG, e)
                }
            })
        }

    fun onInitComplete() {

        lateinit var catsList: List<CatsImage>
        val job = CoroutineScope(Dispatchers.Default)
            .launch {
                val jobFact =
                    CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).async { diConteiner.service.getCatFact() }

                val jobImage = async {
                    catsList = emptyList()
                    try {
                        catsList = diConteiner.serviceImage.getCatImage()
                    } catch (e: SocketTimeoutException) {
                        ErrorMessage(e," SocketTimeoutException")
                        CrashMonitor.trackWarning(TAG, e)
                    } catch (e: Exception) {
                        ErrorMessage(e,"Не удалось получить ответ от сервера")
                        CrashMonitor.trackWarning(TAG, e)
                    }
                    catsList
                }

                withContext(presenterScope.coroutineContext) {
                    if (!jobImage.await().isEmpty()) {
                        _catsLiveData.value =
                            Result.Success(CatsModel(jobFact.await(), catsList))
                    }
                }
            }
        if (job.isCompleted)
            job.cancel()
    }

    fun ErrorMessage(e: Exception, err: String){
        presenterScope.launch{
            _catsLiveData.value =  Result.Error(e, err)
        }
    }
}