package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val meowService: MeowService
) : ViewModel() {

    private val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ -> CrashMonitor.trackWarning() }) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { meowService.getImage() }
                _result.value = Result.Success(UiState(fact.await().text, image.await().url))
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> _result.value = Result.Error("Не удалось получить ответ от сервером")
                    else -> {
                        _result.value = Result.Error(e.message.toString())
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }
}