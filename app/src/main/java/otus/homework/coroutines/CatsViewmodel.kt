package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewmodel(
    private val catsService: CatsService
) : ViewModel() {

    private val _data = MutableLiveData<Result<FactAndImage>>()
    val data: LiveData<Result<FactAndImage>> = _data

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }

    fun getData() {
        viewModelScope.launch(errorHandler) {
            val factJob = async { catsService.getCatFact() }
            val imageJob = async { catsService.getCatImage() }

            try {
                _data.value = Result.Success(
                    FactAndImage(
                        factJob.await(),
                        imageJob.await()
                    )
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                when (e) {
                    is SocketTimeoutException -> {
                        _data.value = Result.Error("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        _data.value = Result.Error(e.message)
                    }
                }
            }
        }
    }
}