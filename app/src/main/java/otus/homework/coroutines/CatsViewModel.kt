package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {
    
    private var dataLoadJob: Job? = null
    
    private val _catsUiState = MutableLiveData<Result<CatsUiState>>()
    val catsUiState: LiveData<Result<CatsUiState>> = _catsUiState
    
    init {
        loadCatData()
    }
    
    fun onButtonClick() = loadCatData()
    
    private fun loadCatData() {
        dataLoadJob?.cancel()
        dataLoadJob = viewModelScope.launch(CoroutineExceptionHandler { _, e ->
            showError(e.message ?: "Неизвестная ошибка")
            CrashMonitor.trackWarning()
        }) {
            val factDeferred = async {
                catsService.getCatFact()
            }
            val imageDeferred = async {
                catsService.getCatImage()
            }
            try {
                val state = CatsUiState(factDeferred.await().text, imageDeferred.await().url)
                _catsUiState.value = Result.Success(state)
            } catch (e: SocketTimeoutException) {
                showError("Не удалось получить ответ от сервера")
            }
        }
    }
    
    private fun showError(message: String) {
        _catsUiState.value = Result.Error(message) // TODO: should be separate LiveEvent or non-exclusive UI model for Success and Error
    }
    
}