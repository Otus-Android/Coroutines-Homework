package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsViewModel(
    private val catsService: CatsService,
    private val imageService: ImageService
) : ViewModel() {
    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { context, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                _catsView?.showSocketExceptionMsg()
            }
            else -> {
                CrashMonitor.trackWarning(ex)
                _catsView?.showGenericErrorMsg()
            }
        }
    }

    fun onViewInitializationComplete() {
        viewModelScope.launch(exceptionHandler + SupervisorJob()) {
            val imageDeferred = async(Dispatchers.IO) { imageService.getCatImage() }
            val factDeferred = async(Dispatchers.IO) { catsService.getCatFact() }

            val imageResponse = imageDeferred.await()
            val factResponse = factDeferred.await()
            val cat = CatInfo(url = imageResponse.url, text = factResponse.text)

            _catsView?.handleResponse(Success(cat))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    override fun onCleared() {
        super.onCleared()
        _catsView = null
    }
}