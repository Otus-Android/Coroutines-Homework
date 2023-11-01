package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsFactsService: CatsFactsService,
    private val imagesService: CatsImagesService,
) : ViewModel(), ICatsInteractor {
    private val imageLoader by lazy { Picasso.get() }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val loadingDispatcher = Dispatchers.IO.limitedParallelism(10)
    private val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        val errorMessage = if (throwable !is SocketTimeoutException) {
            CrashMonitor.trackWarning()
            "Не удалось получить ответ от сервера"
        } else {
            throwable.message ?: "Не удалось получить ответ от сервера"
        }
        _state.value = Result.Error(errorMessage)

        viewModelScope.launch(context) {
            delay(20L)
            _state.value = null
        }
    }

    private val _state = MutableStateFlow<Result<Cat>?>(null)
    val state = _state.asStateFlow()

    override fun onInitComplete() {
        viewModelScope.launch(loadingDispatcher + exceptionHandler) {
            val cat = imagesService.getCatImage().first()
            val imageBitmap = imageLoader.load(cat.imageUrl).get()

            _state.value = Result.Success(
                Cat(
                    image = imageBitmap,
                    text = catsFactsService.getCatFact().text
                )
            )
        }
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    companion object {
        val factory: (CatsFactsService, CatsImagesService) -> ViewModelProvider.Factory =
            { catsFactsService, catsImagesService ->

                viewModelFactory {
                    initializer {
                        CatsViewModel(
                            catsFactsService,
                            catsImagesService
                        )
                    }
                }
            }
    }
}