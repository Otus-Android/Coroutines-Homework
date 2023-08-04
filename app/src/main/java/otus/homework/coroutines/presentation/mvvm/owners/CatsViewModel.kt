package otus.homework.coroutines.presentation.mvvm.owners

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.presentation.mvvm.owners.models.CatUiState
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import otus.homework.coroutines.utils.coroutines.Dispatcher
import java.net.SocketTimeoutException

/**
 * [ViewModel] получения информации о случайном коте
 *
 * @param repository репозиторий информации о кошке
 * @param stringProvider поставщик строковых значений
 * @param dispatcher обертка получения `coroutine dispatcher`
 */
class CatsViewModel(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
    private val dispatcher: Dispatcher
) : ViewModel() {

    /** UI состояние информации о случайном коте */
    val uiState: StateFlow<CatUiState> get() = _uiState.asStateFlow()
    private val _uiState = MutableStateFlow<CatUiState>(CatUiState.Idle)

    /** Событие ошибки */
    val errorEvents get() = _errorEvent.receiveAsFlow()
    private val _errorEvent = Channel<String>(CONFLATED)

    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e)
        viewModelScope.launch(dispatcher.mainImmediate) {
            _errorEvent.send(
                stringProvider.getString(R.string.unexpected_request_error, e.messageOrDefault())
            )
        }
    }

    private fun Throwable.messageOrDefault() =
        this.message ?: stringProvider.getString(R.string.default_request_error)

    /**
     * Получить информацию о случайном коте
     *
     * @param force признак форсированной загрузки.
     * Принудительное выполнение загрузки - `true`. Ленивое выполнение загрузки - `false`
     * (загрузка будет выполняться только в случае, если она еще не проводилась)
     */
    fun getRandomCat(force: Boolean) {
        if (!force && uiState.value !is CatUiState.Idle) {
            return
        }

        if (job?.isActive == true) {
            viewModelScope.launch(dispatcher.mainImmediate) {
                _errorEvent.send(stringProvider.getString(R.string.active_request_warning))
            }
            return
        }

        job = viewModelScope.launch(exceptionHandler + CoroutineName("CatsCoroutine")) {
            try {
                val catInfo = repository.getCatInfo()
                _uiState.update { CatUiState.Success(catInfo) }
            } catch (e: SocketTimeoutException) {
                withContext(dispatcher.mainImmediate) {
                    _errorEvent.send(stringProvider.getString(R.string.timeout_server_error))
                }
            }
        }
    }


    companion object {

        /**
         * Получить фабрику по созданию [CatsViewModel].
         *
         * @param repository репозиторий информации о кошке
         * @param stringProvider поставщик строковых значений
         * @param dispatcher обертка получения `coroutine dispatcher`
         */
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            repository: CatRepository,
            stringProvider: StringProvider,
            dispatcher: Dispatcher
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(repository, stringProvider, dispatcher) as T
            }
        }
    }
}