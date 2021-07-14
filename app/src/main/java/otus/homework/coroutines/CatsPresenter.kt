package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    private val exceptionHandler = CoroutineExceptionHandler { context, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                _catsView?.showToastByException(ex)
            }
            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showToastByException(ex as Exception)
            }
        }
    }

    fun onInitComplete() {
        scope.launch(exceptionHandler) {
            val imageDeferred = async { imageService.getCatImage() }
            val factDeferred = async { catsService.getCatFact() }

            val imageResponse = imageDeferred.await()
            val factResponse = factDeferred.await()
            val cat = CatInfo(url = imageResponse.url, text = factResponse.text)

            _catsView?.populate(cat)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.cancel()
        _catsView = null
    }

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}