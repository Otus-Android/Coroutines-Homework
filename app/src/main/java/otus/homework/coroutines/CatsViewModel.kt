package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.Fact
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
        crashMonitor.trackWarning(TAG, exception.message, exception.stackTraceToString())
        _viewState.value = Result.Error(exception)
    }

    private var loadingFactJob: Deferred<Fact>? = null
    private var loadingImageJob: Deferred<List<CatImage>>? = null

    init {
        loadCatFact()
    }

    fun loadCatFact() = viewModelScope.launch(handler) {
        loadingFactJob?.cancel()
        loadingImageJob?.cancel()
        loadingFactJob = viewModelScope.async {
            catFactService.getCatFact()
        }
        loadingImageJob = viewModelScope.async {
            catImageService.getCatImage()
        }
        try {
            val fact = loadingFactJob?.await()?.fact
            val imageUrl = loadingImageJob?.await()?.first()?.url
            if (fact != null && imageUrl != null) {
                _viewState.value = Result.Success(CatFact(fact, imageUrl))
            }
        } catch (ex: SocketTimeoutException) {
            _viewState.value = Result.SocketTimeoutException(ex)
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



