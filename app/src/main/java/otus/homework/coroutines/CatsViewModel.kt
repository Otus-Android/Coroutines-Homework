package otus.homework.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val meowService: MeowService
) : ViewModel() {

    val result = MutableLiveData<Result>()

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ -> CrashMonitor.trackWarning() }) {
            try {
                val fact = async(Dispatchers.IO) { catsService.getCatFact() }
                val image = async(Dispatchers.IO) { meowService.getImage() }
                result.value = Result.Success(UiState(fact.await().text, image.await().url))
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> result.value = Result.Error("Не удалось получить ответ от сервером")
                    else -> {
                        result.value = Result.Error(e.message.toString())
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }
}