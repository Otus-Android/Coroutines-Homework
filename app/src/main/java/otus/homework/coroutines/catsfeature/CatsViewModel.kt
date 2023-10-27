package otus.homework.coroutines.catsfeature

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.DiContainer
import java.net.SocketTimeoutException

class CatsViewModel(
    private val getCatInfoUseCase: GetCatInfoUseCase,
    private val crashMonitor: CrashMonitor,
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        crashMonitor.trackError("error", throwable)
    }
    private val _actions: MutableSharedFlow<CatsAction> = MutableSharedFlow()
    private val _events: MutableSharedFlow<CatsEvent> = MutableSharedFlow()
    private val _state: MutableStateFlow<CatsState> = MutableStateFlow(CatsState())

    val events = _events.asSharedFlow()
    val state = _state.asStateFlow()

    init {
        _actions
            .combine(_state, ::reduce)
            .distinctUntilChanged()
            .onEach(_state::emit)
            .launchIn(viewModelScope.plus(exceptionHandler))

        _actions.filterIsInstance<CatsAction.Internal.Failure>()
            .map(::handleError)
            .onEach(_events::emit)
            .launchIn(viewModelScope.plus(exceptionHandler))

        dispatch(CatsAction.Ui.Refresh)
    }

    @MainThread
    fun dispatch(action: CatsAction.Ui) {
        viewModelScope.launch(exceptionHandler) {
            _actions.emit(action)
        }
    }

    private fun reduce(action: CatsAction, state: CatsState): CatsState {
        crashMonitor.trackInfo("action=$action, state=$state")
        return when (action) {
            is CatsAction.Internal.Failure -> state.copy(loading = false)
            CatsAction.Internal.Loading -> state.copy(loading = true)
            is CatsAction.Internal.Success -> state.copy(loading = false, data = action.catInfo)
            CatsAction.Ui.Refresh -> {
                viewModelScope.launch(exceptionHandler) {
                    _actions.emit(getCatInfoUseCase.bind(action, this))
                }
                state
            }
        }
    }

    private fun handleError(action: CatsAction.Internal.Failure): CatsEvent {
        crashMonitor.trackError("error", action.error)
        return when (val error = action.error) {
            is SocketTimeoutException -> {
                CatsEvent.Error("Не удалось получить ответ от сервера")
            }

            else -> {
                CatsEvent.Error(error.message ?: "Произошла неизвестная ошибка")
            }
        }
    }

    companion object {
        fun provideFactory(diContainer: DiContainer): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CatsViewModel(
                    getCatInfoUseCase = diContainer.getCatcInfoUseCase(),
                    crashMonitor = diContainer.getCrashMonitor("CatsViewModel")
                )
            }
        }
    }
}

data class CatsState(
    val loading: Boolean = false,
    val data: CatInfo? = null
)

sealed interface CatsEvent {
    data class Error(val cause: String) : CatsEvent
}

sealed interface CatsAction {
    sealed interface Ui : CatsAction {
        object Refresh : Ui
    }

    sealed interface Internal : CatsAction {
        object Loading : Internal
        data class Success(val catInfo: CatInfo) : Internal
        data class Failure(val error: Throwable) : Internal
    }
}