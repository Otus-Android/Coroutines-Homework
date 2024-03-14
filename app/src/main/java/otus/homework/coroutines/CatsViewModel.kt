package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import otus.homework.coroutines.utils.Result

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImage: CatsImage,
) : ViewModel() {


    private val _catFact = MutableStateFlow<Result<Fact>>(Result.Default)
    val catFact = _catFact as StateFlow<Result<Fact>>
    private val factCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            if (throwable is SocketTimeoutException)
                _catFact.value = Result.Error("Не удалось получить ответ от сервера")
            else {
                CrashMonitor.trackWarning()
                _catFact.value = Result.Error(throwable.message)
            }
        }
    }
    fun getCatFact() {
        viewModelScope.launch(factCoroutineExceptionHandler) {
            runCatching {
                _catFact.value = Result.Success(catsService.getCatFact())
            }.onFailure { throwable ->
                CrashMonitor.trackWarning()
                _catFact.value = Result.Error(throwable.message)
            }
        }
    }

    private val _catImage = MutableStateFlow<Result<CatImage>>(Result.Default)
    val catImage = _catImage as StateFlow<Result<CatImage>>

    private val imageCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            if (throwable is SocketTimeoutException)
                _catImage.value = Result.Error("Не удалось получить ответ от сервера")
            else {
                CrashMonitor.trackWarning()
                _catImage.value = Result.Error(throwable.message)
            }
        }
    }

    fun getCatImage() {
        viewModelScope.launch(imageCoroutineExceptionHandler) {
            runCatching {
                _catImage.value = Result.Success(catsImage.getCatImage().first())
            }.onFailure { throwable ->
                CrashMonitor.trackWarning()
                _catImage.value = Result.Error(throwable.message)
            }
        }
    }
}