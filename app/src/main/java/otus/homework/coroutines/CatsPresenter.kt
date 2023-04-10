package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.launch
import kotlinx.coroutines.cancel
import kotlinx.coroutines.Job
import otus.homework.coroutines.model.CatFact
import otus.homework.coroutines.service.CatFactService
import otus.homework.coroutines.service.CatImageService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatFactService,
    private val catImageService: CatImageService,
    private val crashMonitor: CrashMonitor
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var loadingJob: Job? = null

    fun onInitComplete() {
        loadingJob?.cancel()
        loadingJob = presenterScope.launch {
            try {
                val catFact = catFactService.getCatFact()
                val catImage = catImageService.getCatImage()
                _catsView?.populate(CatFact(catFact.fact, catImage.first().url))
            } catch (ex: SocketTimeoutException) {
                _catsView?.showErrorToast(true)
            } catch (ex: Exception) {
                crashMonitor.trackWarning(TAG, ex.message, ex.stackTraceToString())
                _catsView?.showErrorToast(false, ex.message)
            }
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