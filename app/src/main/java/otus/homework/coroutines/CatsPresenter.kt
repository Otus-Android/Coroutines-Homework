package otus.homework.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

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
            } catch (e: SocketTimeoutException) {
                _catsView?.showServerResponseError()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                e.message?.let {
                    _catsView?.showError(it)
                }
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
}