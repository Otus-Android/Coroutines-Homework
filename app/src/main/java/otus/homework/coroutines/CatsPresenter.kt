package otus.homework.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatImageService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val crashMonitor: CrashMonitor,
    private val imageLoader: ImageLoader
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob())

    private var loadingFactJob: Deferred<Fact>? = null
    private var loadingImageJob: Deferred<List<CatImage>>? = null

    fun onInitComplete() = presenterScope.launch {
        loadingFactJob?.cancel()
        loadingImageJob?.cancel()
        loadingFactJob = presenterScope.async {
            catFactService.getCatFact()
        }
        loadingImageJob = presenterScope.async {
            catImageService.getCatImage()
        }
        try {
            val fact = loadingFactJob?.await()?.fact
            val imageUrl = loadingImageJob?.await()?.first()?.url
            if (fact != null && imageUrl != null) {
                _catsView?.populate(CatFact(fact, imageUrl), imageLoader)
            }
        } catch (ex: SocketTimeoutException) {
            _catsView?.showSocketTimeoutToast()
        } catch (ex: Exception) {
            crashMonitor.trackWarning(TAG, ex.message, ex.stackTraceToString())
            _catsView?.showErrorToast(ex.message)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        presenterScope.cancel()
    }

    companion object {
        const val TAG = "CatFact"
    }
}