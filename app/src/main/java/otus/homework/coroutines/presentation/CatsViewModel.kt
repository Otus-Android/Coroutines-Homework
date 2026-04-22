package otus.homework.coroutines.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Error
import otus.homework.coroutines.model.Loading
import otus.homework.coroutines.model.Result
import otus.homework.coroutines.model.Success
import otus.homework.coroutines.network.CatsService
import java.net.SocketTimeoutException

/**
 * ViewModel для экрана с котами.
 * Реализовано согласно ТЗ по Coroutines: использование viewModelScope,
 * обработка исключений через CoroutineExceptionHandler.
 */
class CatsViewModel(
    private val catsService: CatsService
) : ViewModel() {

    private val _state = MutableLiveData<Result<CatsData>>()
    
    /** LiveData состояния экрана (Loading, Success, Error) */
    val state: LiveData<Result<CatsData>> = _state

    private var fetchJob: Job? = null

    /**
     * Обработчик исключений. Согласно ТЗ логирует ошибку в CrashMonitor.
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _state.value = Error(throwable.message ?: "Unknown error")
    }

    /**
     * Запускает получение данных. 
     * Использует viewModelScope, который автоматически отменяется при уничтожении ViewModel.
     */
    fun onInitComplete() {
        fetchJob?.cancel() 
        
        fetchJob = viewModelScope.launch(exceptionHandler) {
            _state.value = Loading
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsService.getCatImage().first() }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                _state.value = Success(CatsData(fact, image))
            } catch (_: SocketTimeoutException) {
                _state.value = Error("Не удалось получить ответ от сервера")
            }
        }
    }
}