package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainViewModel(
    private val catsService: CatsService,
    private val catsImage: CatsImage,
) : ViewModel() {

    private val _state = MutableStateFlow<Result?>(null)
    val state = _state as StateFlow<Result?>

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            if (throwable is SocketTimeoutException) {
                _state.emit(Result.Error(Throwable("Не удалось получить ответ от сервером")))
            } else {
                CrashMonitor.trackWarning()
                _state.emit(Result.Error(throwable))
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                launch {
                    val resultInfo = catsService.getCatFact()
                    _state.emit(Result.Success(resultInfo, null))
                }
                launch {
                    val resultImage = catsImage.getCatImage()
                    _state.emit(Result.Success(null, resultImage[0].url))

                }
            }.onFailure {
                CrashMonitor.trackWarning()
                _state.emit(Result.Error(it))
            }
        }
    }
}
