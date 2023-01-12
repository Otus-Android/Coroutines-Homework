package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel : ViewModel() {

    var catsService: CatsService? = null
    var meowService: MeowService? = null

    val uiState = MutableLiveData<UiState>()
    val errorState = MutableLiveData<String>()

    private var job: Job = Job()

    fun onInitComplete() {
        job.cancel()
        if (catsService == null || meowService == null) return
        job = viewModelScope.launch {
            try {
                val fact = async(Dispatchers.IO) {
                    catsService!!.getCatFact()
                }
                val imageUrl = async(Dispatchers.IO) {
                    meowService!!.getMeow().file
                }
                uiState.value = UiState(fact.await(), imageUrl.await())
            } catch (e: Exception) {
                when(e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> errorState.value = "Не удалось получить ответ от сервера"
                    else -> {
                        CrashMonitor.trackWarning()
                        errorState.value = e.message ?: ""
                    }
                }
            }
        }
    }

    fun release() {
        job.cancel()
    }
}