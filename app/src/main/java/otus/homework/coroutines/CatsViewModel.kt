package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    private var exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable)
        }

    private val _facts: MutableLiveData<Result> = MutableLiveData<Result>()
    val facts: LiveData<Result>
        get() {
            return _facts
        }

    fun fetchFactAndImage() {

        viewModelScope.launch(exceptionHandler + CoroutineName("ViewModel")) {
            supervisorScope {
                try {
                    val fact = async(Dispatchers.IO) {
                        catsService.getCatFact()
                    }

                    val image = async(Dispatchers.IO) {
                        catsService.getCatImage()
                    }

                    val result = awaitAll(fact, image)

                    if (result.size == 2) {
                        val factAndImage = FactAndImage(result[0] as Fact?, result[1] as Image?)
                        _facts.value = Success(factAndImage)

                    } else if (result[0] is Throwable) {
                        throw result[0] as Throwable
                    }

                } catch (exception: Exception) {
                    when (exception) {
                        is CancellationException -> throw exception
                        is SocketTimeoutException ->
                            _facts.value = Error("Не удалось получить ответ от сервера")
                        else -> {
                            CrashMonitor.trackWarning(exception)
                            _facts.value = Error(exception.message)
                        }
                    }
                }
            }
        }
    }
}