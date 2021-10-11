package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {
    val state: LiveData<Result<CatsViewState>>
        get() = _state
    private val _state = MutableLiveData<Result<CatsViewState>>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        _state.value = Error(throwable.message ?: FAILED_DOWNLOAD_MESSAGE)
    }

    fun onInitComplete() = viewModelScope.launch(exceptionHandler) {
        try {
            _state.value = loadData()
        } catch (e: SocketTimeoutException) {
            _state.value = Error(FAILED_DOWNLOAD_MESSAGE)
        }
    }

    private suspend fun loadData(): Success<CatsViewState> = coroutineScope {
        val fact = async { catsService.getCatFact() }
        val image = async { imageService.getRandomImage().file }
        Success(CatsViewState(image.await(), fact.await()))
    }

    companion object {
        private const val FAILED_DOWNLOAD_MESSAGE = "Не удалось загрузить данные с сервера"
    }

    class CatsViewModelProvider(
        private val catsService: CatsService,
        private val imageService: ImageService
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                CatsViewModel(catsService, imageService) as T
            } else {
                throw IllegalArgumentException("Illegal ViewModel")
            }
    }
}