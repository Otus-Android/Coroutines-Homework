package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatImageService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val crashMonitor: CrashMonitor
) : ViewModel() {

    private val _viewState = MutableStateFlow<Result>(Result.Loading)
    val viewState: StateFlow<Result> = _viewState

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> _viewState.value = Result.SocketTimeoutException(exception)
            else -> {
                crashMonitor.trackWarning(TAG, exception.message, exception.stackTraceToString())
                _viewState.value = Result.Error(exception)
            }
        }
    }

    private var loadingJob: Job? = null

    init {
        loadCatFact()
    }

    fun loadCatFact() {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch(handler) {
            val catFact = catFactService.getCatFact()
            val catImage = catImageService.getCatImage()
            _viewState.value = Result.Success(CatFact(catFact.fact, catImage.first().url))
        }
    }

    class Factory(
        private val catFactService: CatFactService,
        private val catImageService: CatImageService,
        private val crashMonitor: CrashMonitor
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>
        ): T {
            return CatsViewModel(
                catFactService,
                catImageService,
                crashMonitor
            ) as T
        }
    }

    companion object {
        const val TAG = "CatFact"
    }
}

sealed class Result {
    object Loading : Result()
    class Success(val catFact: CatFact) : Result()
    class SocketTimeoutException(val exception: Throwable) : Result()
    class Error(val exception: Throwable) : Result()
}



