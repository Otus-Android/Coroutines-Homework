package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private var job: Job? = null
    private val _state = MutableStateFlow<Result<CatsInfo>>(Result.None)
    internal val state: StateFlow<Result<CatsInfo>> = _state.asStateFlow()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
        handelError(throwable)
    }

    fun onInitComplete() {
        job?.cancel()
        _state.value = Result.None
        job = viewModelScope.launch(exceptionHandler) {
            coroutineScope {
                val fact = async {
                    catsService.getCatFact()
                }
                val image = async {
                    catsService.getRandomImg(IMG_URL).getOrNull(0)
                        ?: throw NullPointerException()
                }
                val factResult = fact.await()
                val imageResult = image.await()
                _state.value = Result.Success(CatsInfo(
                    fact = factResult,
                    img = imageResult,
                ))
            }
        }
    }

    private fun handelError(e: Throwable) {
        CrashMonitor.trackWarning()
        if (e is CancellationException) throw e
        val errorMsg = when (e) {
            is java.net.SocketTimeoutException -> "Не удалось получить ответ от сервером"
            else -> e.message
        }
        errorMsg?.let { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                _state.value = Result.Error(errorMsg)
            }
        }
    }

    fun clear() {
        job?.cancel()
        _state.value = Result.None
    }

    companion object {
        const val IMG_URL = "https://api.thecatapi.com/v1/images/search"
    }
}
