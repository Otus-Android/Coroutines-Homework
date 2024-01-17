package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService
) {

    private var presenterScope: CoroutineScope? = createPresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope?.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (ste: java.net.SocketTimeoutException) {
                _catsView?.toast(R.string.http_error_ste)
            } catch (exception: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.toast(R.string.http_error_template, exception.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
        presenterScope = createPresenterScope()
    }

    fun detachView() {
        presenterScope?.cancel()
        _catsView = null
    }

    private fun createPresenterScope(): CoroutineScope {
        presenterScope?.cancel()
        return CoroutineScope(
            Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
        )
    }
}
