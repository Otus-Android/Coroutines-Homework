package otus.homework.coroutines.presentation.viewmodel

import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.models.CatState
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import java.net.SocketTimeoutException

class CatsViewModel(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _resultFlow = MutableStateFlow<CatState>(CatState.Idle)
    val resultLiveData: LiveData<CatState> = _resultFlow.asLiveData()

    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e)
        _resultFlow.update {
            CatState.Error(
                stringProvider.getString(
                    R.string.unexpected_request_error, e.messageOrDefault()
                )
            )
        }
    }

    fun getRandomCat() {
        if (job?.isActive == true) {
            _resultFlow.update { R.string.active_request_warning.asResultError() }
            return
        }

        job = viewModelScope.launch(exceptionHandler + CoroutineName("CatsCoroutine")) {
            try {
                val catInfo = repository.getCatInfo()
                _resultFlow.update { CatState.Success(catInfo) }
            } catch (e: Exception) {
                onError(e)
                if (e is CancellationException) {
                    throw e
                }
            }
        }
    }

    private fun onError(e: Exception) {
        _resultFlow.update {
            CatState.Error(
                if (e is SocketTimeoutException) {
                    stringProvider.getString(R.string.timeout_server_error)
                } else {
                    e.messageOrDefault()
                }
            )
        }
    }

    private fun @receiver:LayoutRes Int.asResultError(): CatState.Error =
        CatState.Error(stringProvider.getString(this))

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