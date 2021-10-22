package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketException
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {
    
    private var dataLoadJob: Job? = null
    
    private val _catsUiState = MutableLiveData<Result<CatsUiState>>()
    val catsUiState: LiveData<Result<CatsUiState>> = _catsUiState
    
    init {
        launchCatDataLoading()
    }
    
    fun onButtonClick() = launchCatDataLoading()
    
    private fun launchCatDataLoading() {
        dataLoadJob?.cancel()
        dataLoadJob = launchSafely {
            _catsUiState.value = Result.Success(loadCatData())
        }
    }
    
    private suspend fun loadCatData(): CatsUiState = coroutineScope {
        val factDeferred = async {
            catsService.getCatFact()
        }
        val imageDeferred = async {
            catsService.getCatImage()
        }
        CatsUiState(factDeferred.await().text, imageDeferred.await().url)
    }
    
    private fun launchSafely(
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(
            CoroutineExceptionHandler { _, e ->
                _catsUiState.value = Result.Error(e.message ?: "Неизвестная ошибка")
                CrashMonitor.trackWarning()
            }
        ) {
            try {
                block()
            } catch (e: SocketTimeoutException) {
                showError("Не удалось получить ответ от сервера")
            } catch (e: SocketException) {
                showError("Не удалось получить ответ от сервера")
            }
        }
    }
    
    private fun showError(message: String) {
        // TODO: should be separate LiveEvent or exclusive UI for Success and Error states
        _catsUiState.value = Result.Error(message)
    }
    
}