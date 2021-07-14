package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {
    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { context, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                _catsView?.handleResponse(Error(ex))
            }
            else -> {
                CrashMonitor.trackWarning(ex)
                _catsView?.handleResponse(Error(ex))
            }
        }
    }

    fun onInitComplete() {
        viewModelScope.launch(exceptionHandler) {
            val imageDeferred = async { imageService.getCatImage() }
            val factDeferred = async { catsService.getCatFact() }

            val imageResponse = imageDeferred.await()
            val factResponse = factDeferred.await()
            val cat = CatInfo(url = imageResponse.url, text = factResponse.text)

            _catsView?.handleResponse(Success(cat))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}