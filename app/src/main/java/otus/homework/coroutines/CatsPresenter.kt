package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val scope = PresenterScope()
    private var _catsView: ICatsView? = null
    fun onInitComplete() {
        scope.launch {
            try {
                val catFact = catsService.getCatFact()
                _catsView?.populate(catFact)
            } catch (e: SocketTimeoutException) {
                _catsView?.message(R.string.failed_response_server)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.message(e.message.toString())
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
}

class PresenterScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
}
