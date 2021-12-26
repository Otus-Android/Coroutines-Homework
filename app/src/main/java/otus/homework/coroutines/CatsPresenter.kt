package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val scope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch {
            try {
                _catsView?.populate(catsService.getCatFact())
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsView?.error("Failed to get a response from the server")
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.error(ex.message ?: "")
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
}