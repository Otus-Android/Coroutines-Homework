package otus.homework.coroutines.presentation.mvvm

import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.models.presentation.CatUiState
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import java.net.SocketTimeoutException

class CatsViewModel(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CatUiState>(CatUiState.Idle)
    val uiState: StateFlow<CatUiState> = _uiState.asStateFlow()

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
            } catch (e: Exception) {
                onError(e)
                if (e is CancellationException) {
                    throw e
                }
            }
        }
    }

    fun onErrorShown() = _uiState.update { state ->
        if (state is CatUiState.Error) {
            state.copy(isShown = true)
        } else {
            state
        }
    }

    private fun onError(e: Exception) {
        _uiState.update {
            CatUiState.Error(
                if (e is SocketTimeoutException) {
                    stringProvider.getString(R.string.timeout_server_error)
                } else {
                    e.messageOrDefault()
                }
            )
        }
    }

    private fun @receiver:LayoutRes Int.asResultError(): CatUiState.Error =
        CatUiState.Error(stringProvider.getString(this))

    private fun Throwable.messageOrDefault() =
        this.message ?: stringProvider.getString(R.string.default_request_error)


    companion object {

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