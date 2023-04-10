package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatData
import otus.homework.coroutines.data.CatViewData
import java.net.SocketTimeoutException

class CatsViewModel(
    private val repository: CatsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            CrashMonitor.trackWarning(throwable)

            val message = when (throwable) {
                is SocketTimeoutException -> "Не удалось получить ответ от сервером"
                else -> throwable.message ?: "Неизвестная ошибка"
            }
            _uiEvents.emit(UiEvent.ShowError(message))
        }
    }

    init {
        updateFact()
    }

    fun updateFact() {
        viewModelScope.launch(exceptionHandler) {
            val data = repository.getCatData()
            _uiState.emit(UiState.Success(data.mapToViewData()))
        }
    }

    private fun CatData.mapToViewData(): CatViewData {
        return CatViewData(
            fact = fact.text,
            imageUrl = image.url
        )
    }

    companion object {
        fun factory(repository: CatsRepository) =
            viewModelFactory {
                initializer { CatsViewModel(repository) }
            }
    }

    sealed class UiEvent {
        data class ShowError(val message: String) : UiEvent()
    }

    sealed interface UiState {
        object Idle : UiState
        class Success(val data: CatViewData) : UiState
    }
}