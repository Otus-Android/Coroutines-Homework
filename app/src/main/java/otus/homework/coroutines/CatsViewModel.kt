package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import otus.homework.coroutines.models.CatsInfo
import java.net.SocketTimeoutException


class CatsViewModel constructor(
    private val catsService: CatsService
) : ViewModel() {

    private val _state = MutableLiveData<Result<CatsInfo>>()
    val state = _state as LiveData<Result<CatsInfo>>

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    fun requestCatsInfo() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val factDeferred = async() { catsService.getCatFact() }
                val imageDeferred =
                    async(Dispatchers.IO) { catsService.getCatImage("https://aws.random.cat/meow") }

                val fact = factDeferred.await().fact
                val image = imageDeferred.await().file
                _state.value = Success(CatsInfo(fact, image))
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: SocketTimeoutException) {
                _state.value = Error("Не удалось получить ответ от сервером")
            } catch (exception: Exception) {
                CrashMonitor.trackWarning(exception)
                _state.value = Error(exception.message)
            }
        }
    }
}