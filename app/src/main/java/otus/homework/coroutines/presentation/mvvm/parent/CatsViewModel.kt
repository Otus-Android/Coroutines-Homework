package otus.homework.coroutines.presentation.mvvm.parent

import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.presentation.mvvm.parent.models.CatUiState
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import java.net.SocketTimeoutException

/**
 * [ViewModel] получения информации о случайном коте
 *
 * @param repository репозиторий информации о кошке
 * @param stringProvider поставщик строковых значений
 */
class CatsViewModel(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
) : ViewModel() {

    /** UI состояние информации о случайном коте */
    val uiState: StateFlow<CatUiState> get() = _uiState.asStateFlow()
    private val _uiState = MutableStateFlow<CatUiState>(CatUiState.Idle)

    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e)
        _uiState.update {
            CatUiState.Error(
                stringProvider.getString(
                    R.string.unexpected_request_error, e.messageOrDefault()
                )
            )
        }
    }

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
            _uiState.update { R.string.active_request_warning.asResultError() }
            return
        }

        job = viewModelScope.launch(exceptionHandler + CoroutineName("CatsCoroutine")) {
            try {
                val catInfo = repository.getCatInfo()
                _uiState.update { CatUiState.Success(catInfo) }
            } catch (e: SocketTimeoutException) {
                _uiState.update {
                    CatUiState.Error(stringProvider.getString(R.string.timeout_server_error))
                }
            }
        }
    }

    /** Обработать отображение ошибки */
    fun onErrorShown() = _uiState.update { state ->
        if (state is CatUiState.Error) {
            state.copy(isShown = true)
        } else {
            state
        }
    }

    private fun @receiver:LayoutRes Int.asResultError(): CatUiState.Error =
        CatUiState.Error(stringProvider.getString(this))

    private fun Throwable.messageOrDefault() =
        this.message ?: stringProvider.getString(R.string.default_request_error)


    companion object {

        /**
         * Получить фабрику по созданию [CatsViewModel].
         *
         * @param repository репозиторий информации о кошке
         * @param stringProvider поставщик строковых значений
         */
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
            repository: CatRepository,
            stringProvider: StringProvider,
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(repository, stringProvider) as T
            }
        }
    }
}