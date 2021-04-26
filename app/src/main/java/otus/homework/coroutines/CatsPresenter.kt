package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) _catsView?.showNetworkError() else _catsView?.showError(e.message.orEmpty())
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        presenterScope.launch(exceptionHandler) {
            val response = presenterScope.async {
                withContext(Dispatchers.IO) { catsService.getCatFact() }
            }.await()
            if (response.isSuccessful) response.body()?.let { _catsView?.populate(it) }
            else CrashMonitor.trackWarning()
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