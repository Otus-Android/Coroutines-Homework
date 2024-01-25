package otus.homework.coroutines

import android.content.res.Resources
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: CatImageService,
    private val resManager: Resources
) {

    private var _catsView: ICatsView? = null

    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job? = null

    fun onInitComplete() {
        job?.cancel()
        job = scope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val fact = async {
                        catsService.getCatFact()
                    }
                    val image = async {
                        imageService.getCatImages().firstOrNull()
                    }
                    FactState(
                        fact = fact.await(),
                        image = image.await()
                    )
                }
                _catsView?.populate(result)
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsView?.showError(resManager.getString(R.string.socket_timeout_error))
                } else {
                    CrashMonitor.trackWarning()
                    _catsView?.showError(e.message ?: resManager.getString(R.string.unknown_error))
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null

        job?.cancel()
        job = null
    }
}