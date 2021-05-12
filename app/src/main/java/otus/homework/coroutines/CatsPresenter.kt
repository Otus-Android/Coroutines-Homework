package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.api.CatsServiceFact
import otus.homework.coroutines.api.CatsServiceImage
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsServiceFact: CatsServiceFact,
    private val catsServiceImage: CatsServiceImage
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val requestImage = presenterScope.async { withContext(Dispatchers.IO) { catsServiceImage.getCatImage() } }
                val requestFact = presenterScope.async { withContext(Dispatchers.IO) { catsServiceFact.getCatFact() } }
                val image = requestImage.await()
                val fact = requestFact.await()
                _catsView?.populate(fact, image.getImageUrl())
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