package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        when (exception) {
            is SocketTimeoutException -> _resultFlow.value = Result.Error(exception)
            else -> {
                CrashMonitor.trackWarning()
                _resultFlow.value = Result.Error(exception)
            }
        }
    }

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val fact: Fact
            val images: List<Image>
            withContext(Dispatchers.IO) {
                val factDeferred = async { catsService.getCatFact() }
                val imagesDeferred = async { imagesService.getImages() }
                fact = factDeferred.await()
                images = imagesDeferred.await()
            }
            _resultFlow.value = Result.Success(CatsUiModel(fact, images.first()))
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
