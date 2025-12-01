package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor.trackWarning
import java.net.SocketTimeoutException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow


class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val onShowToast: (String?) -> Unit
) : ViewModel() {

    private val _state = MutableStateFlow<Result>(Result.Error("No fact found"))

    private val handler = CoroutineExceptionHandler { _, exception ->
        if(exception is SocketTimeoutException){
            onShowToast("Не удалось получить ответ от сервера")
        }else {
            trackWarning("CoroutineExceptionHandler got $exception")
            onShowToast(exception.message)
        }
        _state.value = Result.Error(exception.message)
    }

    fun onInitComplete() {
        viewModelScope.launch (handler){
            val fact = async { catsService.getCatFact() }
            val image = async { imageService.getImage().firstOrNull() }
            val model = CatDataModel(fact.await(), image.await()?.url)
            _state.value = Result.Success(model)
        }
    }
}

sealed class Result {
    data class Success<T>(val data: T) : Result()
    data class Error(val msg: String?) : Result()
}
