package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {
    private val _uiModelFlow = MutableStateFlow<Result>(Result.Loading)
    val uiModelFlow: StateFlow<Result> = _uiModelFlow.asStateFlow()
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        when (e) {
            is SocketTimeoutException -> {
                _uiModelFlow.tryEmit(Result.Error(message = "Не удалось получить ответ от сервера"))
            }

            else -> {
                CrashMonitor.trackWarning()
                _uiModelFlow.tryEmit(Result.Error(message = e.message))
            }
        }
    }

    fun load() {
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch(exceptionHandler) {
            _uiModelFlow.emit(Result.Loading)
            val factJob = async(Dispatchers.IO) {
                val response = catsService.getCatFact()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            }

            val imageJob = async(Dispatchers.IO) {
                val response = catsService.getRandomImage()
                if (response.isSuccessful) {
                    response.body()?.firstOrNull()
                } else {
                    null
                }
            }

            val factResult = factJob.await()
            val imageResult = imageJob.await()
            _uiModelFlow.emit(
                Result.Success(
                    CatsUiModel(
                        fact = factResult?.fact,
                        image = imageResult?.url
                    )
                )
            )
        }
    }

    class Factory(private val catsService: CatsService) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService) as T
        }
    }
}