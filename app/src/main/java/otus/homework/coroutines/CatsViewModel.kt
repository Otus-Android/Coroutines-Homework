package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel (
    private val catsService: CatsService,
    private val imagesService: ImageService,
    private val onShowToast: (String?) -> Unit
) : ViewModel() {

    private val _state = MutableStateFlow<Result>(Result.Error("Произошла ошибка при получении факта"))
    val state: StateFlow<Result> = _state.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        if(exception is SocketTimeoutException){
            onShowToast("Не удалось получить ответ от сервера")
        }else {
            CrashMonitor.trackWarning("CoroutineExceptionHandler got $exception")
            onShowToast(exception.message)
        }
        _state.value = Result.Error(exception.message)
    }

    fun onInitComplete() {
        viewModelScope.launch (handler){
            val fact = async { catsService.getCatFact() }
            val image = async { imagesService.getCatImage().firstOrNull() }
            val model = CatModel(fact.await().fact, image.await()?.url)
            _state.value = Result.Success(model)
        }
    }
}

sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val msg: String?) : Result()
}