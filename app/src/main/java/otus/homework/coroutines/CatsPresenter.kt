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
            _catsView?.populate(getCatFactWithImage())
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private suspend fun getCatFactWithImage() = coroutineScope {
        val fact = async { getCatFact() }
        val img = async { getCatRandomImage() }
        return@coroutineScope CatsModel(fact.await().fact, img.await().file)
    }

    private suspend fun getCatFact() = withContext(Dispatchers.IO) {
        return@withContext catsService.getCatFact()
    }

    private suspend fun getCatRandomImage() = withContext(Dispatchers.IO) {
        return@withContext catsService.getCatRandomImage()
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