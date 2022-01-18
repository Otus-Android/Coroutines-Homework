package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(private val catsService: CatsService) {
    private lateinit var presenterScope: PresenterScope
    private val scopeExceptionHandler = CoroutineExceptionHandler { _, ex ->
        ex.message?.let { _catsView?.showToast(it) }
        CrashMonitor.trackWarning()
    }
    private var _catsView: ICatsView? = null

    fun attachView(catsView: ICatsView) {
        presenterScope = PresenterScope()
        _catsView = catsView
    }

    fun onInitComplete() {
        presenterScope.launch(scopeExceptionHandler) {
            fetchCatFact()
        }
    }

    private suspend fun fetchCatFact() {
        try {
            _catsView?.populate(catsService.getCatFact())
        } catch (e: SocketTimeoutException) {
            _catsView?.showToast(R.string.server_not_responding)
        }
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

}
