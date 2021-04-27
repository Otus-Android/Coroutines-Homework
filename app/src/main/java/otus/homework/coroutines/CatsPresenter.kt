package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsServiceFact: CatsServiceFact,
    private val catsServiceImage: CatsServiceImage
) {

    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) _catsView?.showNetworkError() else _catsView?.showError(e.message.orEmpty())
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        presenterScope.launch() {
            try {
                val requestImage = presenterScope.async { withContext(Dispatchers.IO) { catsServiceImage.getCatImage() } }
                val requestFact = presenterScope.async { withContext(Dispatchers.IO) { catsServiceFact.getCatFact() } }
                val responseImage = requestImage.await()
                val responseFact = requestFact.await()
                if (responseFact.isSuccessful && responseImage.isSuccessful) {
                    responseFact.body()?.let { fact ->
                        responseImage.body()
                            ?.let { image -> _catsView?.populate(fact, image.getImageUrl()) }
                    }
                } else CrashMonitor.trackWarning()
            } catch (e: SocketTimeoutException) {
                _catsView?.showNetworkError()
                CrashMonitor.trackWarning()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
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