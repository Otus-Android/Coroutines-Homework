package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val _scope = PresenterScope("CatsCoroutine")

    fun onInitComplete() {
        fetchCatsModel()
    }

    private fun fetchCatsModel() = _scope.launch {
        try {
            val fact = async(Dispatchers.IO) { catsService.getCatFact() }
            val img = async(Dispatchers.IO) { catsService.getCatRandomImage() }
            CatsModel(fact.await().fact, img.await().file)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun handleException(e: Exception) = when (e) {
        is SocketTimeoutException -> _catsView?.showNetworkError()
        else -> {
            CrashMonitor.trackWarning(e)
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