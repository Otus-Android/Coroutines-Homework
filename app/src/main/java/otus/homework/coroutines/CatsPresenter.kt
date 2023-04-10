package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val crashMonitor: CrashMonitor
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val catFact = catsService.getCatFact()
                throw RuntimeException()
                _catsView?.populate(catFact)
            } catch (ex: SocketTimeoutException) {
                _catsView?.showErrorToast(true)
            } catch (ex: Exception) {
                crashMonitor.trackWarning(TAG, ex.message, ex.stackTraceToString())
                _catsView?.showErrorToast(false, ex.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        presenterScope.cancel()
    }

    companion object {
        const val TAG = "CatFact"
    }
}