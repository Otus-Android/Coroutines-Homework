package otus.homework.coroutines.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.repo.Repository
import java.net.SocketTimeoutException

class CatsViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<CatsState>(CatsState.Init)
    val state: StateFlow<CatsState> get() = _state

    private val _events = Channel<CatsEvent> { Channel.RENDEZVOUS }
    val events: Flow<CatsEvent> get() = _events.receiveAsFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)

        viewModelScope.launch {
            if (throwable is SocketTimeoutException) {
                _events.send(element = CatsEvent.Error("Не удалось получить ответ от сервером"))
                return@launch
            }
            _events.send(element = CatsEvent.Error(throwable.message ?: "error"))
        }
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {

        viewModelScope.launch(exceptionHandler) {
            when (val result = repository.getMeowInfo()) {
                is Result.Success -> {
                    _state.value = CatsState.Data(result.data)
                }
                is Result.Error -> {
                    exceptionHandler.handleException(this.coroutineContext, result.e)
                    _state.value = CatsState.Error
                }
            }
        }
    }
}