package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val _scope = PresenterScope("CatsCoroutine")

    fun onInitComplete() {
        fetchCatFact()
    }

    private fun fetchCatFact() = _scope.launch {
        try {
            _catsView?.populate(getCatFact())
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private suspend fun getCatFact() = withContext(Dispatchers.IO) {
        return@withContext catsService.getCatFact()
    }

    private fun handleException(e: Exception) = when (e) {
        is SocketTimeoutException -> _catsView?.showNetworkError()
        else -> {
            CrashMonitor.trackWarning()
            _catsView?.showError(e.message.orEmpty())
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        _scope.cancel()
    }
}