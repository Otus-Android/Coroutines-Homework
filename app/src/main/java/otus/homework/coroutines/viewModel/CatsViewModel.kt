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
    }

    init {
        loadMeowInfo()
    }

    fun loadMeowInfo() {

        viewModelScope.launch(exceptionHandler) {
            when (val result = repository.getMeowInfo()) {
                is Result.Success -> {
                    _state.value = CatsState.Data(result.data)
                }
                is Result.Error -> {

                    if (result.e is SocketTimeoutException) {
                        _events.send(element = CatsEvent.Error("Не удалось получить ответ от сервера"))

                    } else {
                        _events.send(element = CatsEvent.Error(result.e.message ?: "error"))
                    }

                    _state.value = CatsState.Error
                }
            }
        }
    }
}