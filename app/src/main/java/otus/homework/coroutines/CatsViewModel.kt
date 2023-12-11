package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

private const val RESET_DELAY_DURATION_MS = 20L

class CatsViewModel(
    private val catsFactsService: CatsFactsService,
    private val imagesService: CatsImagesService,
) : ViewModel() {

    private val loadingDispatcher = Dispatchers.IO
    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val errorMessage = if (throwable !is SocketTimeoutException) {
            CrashMonitor.trackWarning()
            "Не удалось получить ответ от сервера"
        } else {
            throwable.message ?: "Не удалось получить ответ от сервера"
        }
        _state.value = Result.Error(errorMessage)

        viewModelScope.launch(context) { resetErrorState() }
    }

    private val _state = MutableStateFlow<Result<Cat>?>(null)
    val state = _state.asStateFlow()

    fun loadCat() {
        viewModelScope.launch(loadingDispatcher + exceptionHandler) {
            try {
                val cat = imagesService.getCatImage().first()
                val fact = async { catsFactsService.getCatFact().text }

                _state.value = Result.Success(
                    Cat(
                        imageUrl = cat.imageUrl,
                        text = fact.await()
                    )
                )
            } catch (_: SocketTimeoutException) {
                _state.value = Result.Error(message = "Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _state.value = Result.Error(message = e.message ?: "Не удалось получить ответ от сервера")
                CrashMonitor.trackWarning()
            }

            if (_state.value is Result.Error) resetErrorState()
        }
    }

    private suspend fun resetErrorState() {
        delay(RESET_DELAY_DURATION_MS)
        _state.value = null
    }
}