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
            val job = CoroutineScope(Dispatchers.Main +  coroutineExceptionHandler ).launch {

            val jobImage =
                withContext(Dispatchers.IO) {
                    async {
                        diConteiner.serviceImage.getCatImage()
                    }
                }

            val jobFact =
                withContext(Dispatchers.IO) {
                    async {
                        diConteiner.service.getCatFact()
                    }
                }

            _catsLiveData.value =
                Result.Success(CatsModel(jobFact.await(), jobImage.await()))
        }
        if(job.isCompleted)
                        job.cancel()
    }

}

