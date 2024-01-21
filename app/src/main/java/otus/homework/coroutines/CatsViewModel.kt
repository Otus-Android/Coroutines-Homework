package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import otus.homework.coroutines.Result.Error
import otus.homework.coroutines.Result.Success

class CatsViewModel(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) : ViewModel() {

    private val _state = MutableStateFlow<Result?>(null)

    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
        _state.value = Error(throwable)
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val fact = async { catsService.getCatFact() }
            val images = async { imagesService.getCatImages() }
            _state.value = Success(
                FactWithImage(
                    fact = fact.await().fact,
                    imageUrl = images.await().firstOrNull()?.url.orEmpty()
                )
            )
        }
    }
}