package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) : ViewModel() {

    private val _catsState =
        MutableStateFlow<Result<CatPresentationData>>(Result.Loading)
    val catsState: StateFlow<Result<CatPresentationData>> = _catsState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            val message = when (throwable) {
                is SocketTimeoutException -> "Не удалось получить ответ от сервера"
                else -> throwable.message ?: "Произошла ошибка"
            }
            _catsState.value = Result.Error(message, throwable as? Exception)
            if (throwable is Exception) {
                CrashMonitor.logException(throwable)
            }
        }
    }

    fun loadCatData() {
        viewModelScope.launch(exceptionHandler) {
            _catsState.value = Result.Loading

            val factDeferred = async(Dispatchers.IO) { catsService.getCatFact() }
            val imageDeferred = async(Dispatchers.IO) { catImageService.getRandomCatImage() }

            val fact = factDeferred.await()
            val image = imageDeferred.await()

            val presentationData = CatPresentationData(
                fact = fact.fact,
                imageUrl = image.file
            )

            _catsState.value = Result.Success(presentationData)
        }
    }
}

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
}