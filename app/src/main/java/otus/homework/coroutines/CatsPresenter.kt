package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private val scope = PresenterScope()

    private var _catsView: ICatsView? = null
    private var getCatFactJob: Job? = null

    fun onInitComplete() {
        getCatFactJob = scope.launch {
            try {
                _catsView?.populate(catsService.getCatFact())
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException ->
                        _catsView?.showLoadError("Failed to get a response from the server")
                    else -> {
                        CrashMonitor.trackWarning(ex)
                        _catsView?.showLoadError(ex.message ?: "No error message")
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
        getCatFactJob?.cancel()
    }

    private class PresenterScope : CoroutineScope {

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}
