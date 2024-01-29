package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService
): ViewModel() {

    private val _resultFlow = MutableStateFlow<Result<CatsUiModel>>(
        Result.Success(CatsUiModel(Fact.EMPTY, Image.EMPTY))
    )
    val resultFlow = _resultFlow.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        CrashMonitor.trackWarning()
        _resultFlow.value = Result.Error(exception)
    }

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch(coroutineExceptionHandler) {
            runCatching {
                val fact: Fact
                val images: List<Image>
                val factDeferred = async { catsService.getCatFact() }
                val imagesDeferred = async { imagesService.getImages() }
                fact = factDeferred.await()
                images = imagesDeferred.await()
                fact to images.first()
            }.onSuccess {
                _resultFlow.value = Result.Success(CatsUiModel(it.first, it.second))
            }.onFailure {
                when (it) {
                    is SocketTimeoutException -> _resultFlow.value = Result.Error(it)
                    else -> throw it
                }
            }
        }
    }

    class Factory(
        private val catsService: CatsService,
        private val imagesService: ImagesService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatsViewModel(catsService, imagesService) as T
        }
    }
}
