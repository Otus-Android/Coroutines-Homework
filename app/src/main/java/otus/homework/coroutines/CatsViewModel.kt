package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsViewModel(
    private val catsService: CatsService,
    private val catsPhotoService: CatsPhotoService
) : ViewModel() {

    /** State экрана */
    val screenState: StateFlow<Result<CatsViewState>> get() = _screenState.asStateFlow()
    private val _screenState = MutableStateFlow<Result<CatsViewState>>(Result.Empty())

    fun onInitComplete() {

        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                val fact = getCatFact().await()
                val photo = getCatPhoto().await()
                CatsViewState(
                    fact = fact,
                    photo = photo
                )
            }
                .onFailure {
                    _screenState.emit(Result.Error(it.message))
                    when (it) {
                        is SocketTimeoutException -> {
                            CrashMonitor.trackWarning()
                        }
                        is CancellationException -> {
                            CrashMonitor.trackWarning()
                            throw it
                        }
                        else -> {
                            CrashMonitor.trackWarning()
                        }
                    }
                }
                .onSuccess {
                    _screenState.emit(Result.Success(it))
                }

        }
    }

    private suspend fun getCatFact() = viewModelScope.async { catsService.getCatFact() }

    private suspend fun getCatPhoto() = viewModelScope.async { catsPhotoService.getCatPhoto() }

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }

    class CatsViewModelProviderFactory(
        private val catsService: CatsService,
        private val catsPhotoService: CatsPhotoService
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(
                catsService = catsService,
                catsPhotoService = catsPhotoService
            ) as T
        }
    }
}