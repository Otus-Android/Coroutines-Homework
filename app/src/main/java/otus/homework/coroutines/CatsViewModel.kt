package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.namespace.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val serviceRandomCatFact: CatsService,
    private val serviceRandomCatImage: CatsService
) : ViewModel() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exc -> CrashMonitor.trackWarning(exc) }

    private val _catItem = MutableSharedFlow<CatItem>()
    val catItem = _catItem.asSharedFlow()

    private val _error = MutableSharedFlow<Result.Error>()
    val error = _error.asSharedFlow()

    fun onInitComplete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val fact = async { serviceRandomCatFact.getCatFact() }
                val image = async { serviceRandomCatImage.catImage() }
                _catItem.emit(CatItem(fact.await().fact, image.await().file))
            } catch (e: SocketTimeoutException) {
                _error.emit(Result.Error(messageId = R.string.failed_response_server))
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                _error.emit(Result.Error(text = e.message.toString()))
            }
        }
    }

    companion object {
        fun provideFactory(
            serviceRandomCatFact: CatsService,
            serviceRandomCatImage: CatsService
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatsViewModel(serviceRandomCatFact, serviceRandomCatImage) as T
                }
            }
    }
}
