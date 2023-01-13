package otus.homework.coroutines.ui

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.model.CatsUiState
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.Image
import otus.homework.coroutines.network.CatsService
import otus.homework.coroutines.network.ImageService
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {

    private val _catsState = MutableLiveData<Result<CatsUiState>>()
    val catsState: LiveData<Result<CatsUiState>> = _catsState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
    }

    init {
        onInitComplete()
    }

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            setLoadingState(true)
            supervisorScope {
                val factDeferred = async { loadFact() }
                val imageDeferred = async { loadCatImage() }

                val fact = runCustomCatch { factDeferred.await()?.fact }
                val image = runCustomCatch { imageDeferred.await()?.file }

                _catsState.value = Result.Success(
                    data = CatsUiState(
                        fact = fact ?: "",
                        imageUrl = image
                    )
                )
                setLoadingState(false)
            }
        }
    }

    private suspend fun loadFact(): Fact? {
        val response = catsService.getCatFact()
        return if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

    private suspend fun loadCatImage(): Image? {
        val response = imageService.getCatImage()
        return if (response.isSuccessful && response.body() != null) {
            response.body()
        } else {
            null
        }
    }

    private suspend fun <T> runCustomCatch(block: suspend () -> T?): T? {
        return try {
            block()
        } catch (e: SocketTimeoutException) {
            showMessage(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
            null
        } catch (e: Exception) {
            CrashMonitor.trackWarning(e)
            showMessage(e.message)
            null
        }
    }

    private fun showMessage(message: String?) {
        _catsState.value = Result.Error(message ?: UNKNOWN_ERROR)
    }

    private fun setLoadingState(isLoading: Boolean) {
        _catsState.value = Result.Loading(isLoading)
    }

    companion object {
        private const val UNKNOWN_ERROR = "Неизвестная ошибка"
        private const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val diContainer = DiContainer()

                CatsViewModel(
                    catsService = diContainer.catsService,
                    imageService = diContainer.imageService
                )
            }
        }
    }
}