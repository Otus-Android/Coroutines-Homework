package otus.homework.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: PresenterScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.populate(catsService.getCatFact())
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.displayError(CatsError.NetworkError)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.displayError(CatsError.DefaultError(e.message))
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}