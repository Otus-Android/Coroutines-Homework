package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (ex: SocketTimeoutException) {
                _catsView?.showSocketTimeoutExceptionToast()
            } catch (ex: Exception) {
                CrashMonitor.trackWarning(CatsPresenter::class.simpleName, ex)
                _catsView?.showDefaultExceptionToast(ex.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = Dispatchers.Main + CoroutineName(COROUTINE_NAME) + Job()
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}