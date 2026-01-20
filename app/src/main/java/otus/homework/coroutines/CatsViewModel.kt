package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->

        CrashMonitor.trackWarning()


        _result.value = Result.Error(
            message = when (throwable) {
                is SocketTimeoutException -> "Не удалось получить ответ от сервера"
                else -> throwable.message ?: "Неизвестная ошибка"
            },
            exception = throwable
        )
    }

    // LiveData для состояния
    private val _result = MutableLiveData<Result<Fact>>()
    val result: LiveData<Result<Fact>> = _result

    private var currentJob: Job? = null

    fun loadCatData() {

        currentJob?.cancel()

        currentJob = viewModelScope.launch(exceptionHandler) {
            try {

                _result.value = Result.Loading


                val deferredFact = async { catsService.getCatFact() }
                val deferredImage = async { catsImageService.getCatImage() }


                val (factResult, imageResult) = awaitAll(deferredFact, deferredImage)


                val factWithImage = factResult.copy(imageUrl = imageResult.file)


                _result.value = Result.Success(factWithImage)

            } catch (e: Exception) {

                if (e is SocketTimeoutException) {
                    _result.value = Result.Error("Не удалось получить ответ от сервера", e)
                } else {

                    _result.value = Result.Error(e.message ?: "Неизвестная ошибка", e)
                }
            }
        }
    }

    fun cancelCurrentRequest() {
        currentJob?.cancel()
        currentJob = null
    }

    override fun onCleared() {
        super.onCleared()
        cancelCurrentRequest()
    }
}

