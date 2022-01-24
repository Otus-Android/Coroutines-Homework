package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewmodel(
    private val catsService: CatsService
) : ViewModel() {

    private val _data = MutableLiveData<Result<FactAndImage>>()
    val data: LiveData<Result<FactAndImage>> = _data

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        when (throwable) {
            is SocketTimeoutException -> {
                _data.value = Result.Error("Не удалось получить ответ от сервера")
            }
            else -> {
                _data.value = Result.Error(throwable.message)
            }
        }

        CrashMonitor.trackWarning()
    }

    fun getData() {
        viewModelScope.launch(errorHandler) {
            _data.value = Result.Success(
                FactAndImage(
                    catsService.getCatFact(),
                    catsService.getCatImage()
                )
            )
        }
    }

    fun stop() {
        viewModelScope.cancel()
    }
}