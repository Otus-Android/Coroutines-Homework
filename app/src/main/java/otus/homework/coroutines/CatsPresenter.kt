package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val response = catsService.getCatFact()
                _catsView?.populate(response)
            } catch (ex: java.net.SocketTimeoutException) {
                _catsView?.showToastByException(ex)
            } catch (ex: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToastByException(ex)
            }
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