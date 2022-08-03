package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatsViewModel(
    private val catsService: CatsService,
    private val catsPhotoService: CatsPhotoService
) : ViewModel() {

    /** State экрана */
    val screenState: StateFlow<Result<CatsViewState?>?> get() = _screenState.asStateFlow()
    private val _screenState = MutableStateFlow<Result<CatsViewState?>?>(null)

    private val scope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob())

    fun onInitComplete() {

        scope.launch(coroutineExceptionHandler) {
            runCatching {
                loadData()
            }
                .onFailure {
                    _screenState.emit(Result.Error(it.message))
                }
                .onSuccess {
                    _screenState.emit(Result.Success(it))
                }

        }
    }

    private suspend fun loadData() =
        CatsViewState(
            fact = catsService.getCatFact(),
            photo = catsPhotoService.getCatPhoto()
        )

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