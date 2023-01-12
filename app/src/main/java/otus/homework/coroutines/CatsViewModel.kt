package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    var catsService: CatsService? = null
    var meowService: MeowService? = null

    val state = MutableLiveData<Result>()

    private var job: Job = Job()

    private val exceptionHandler = CoroutineExceptionHandler() { _, _ ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        job.cancel()
        if (catsService == null || meowService == null) return
        job = viewModelScope.launch(exceptionHandler) {
            try {
                val fact = async(Dispatchers.IO) {
                    catsService!!.getCatFact()
                }
                val imageUrl = async(Dispatchers.IO) {
                    meowService!!.getMeow().file
                }
                state.value = Result.Success(UiState(fact.await(), imageUrl.await()))
            } catch (e: Exception) {
                when(e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> state.value = Result.Error("Не удалось получить ответ от сервера")
                    else -> {
                        CrashMonitor.trackWarning()
                        state.value = Result.Error(e.message ?: "")
                    }
                }
            }
        }
    }

    fun release() {
        job.cancel()
    }
}