package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(
        Dispatchers.Main + presenterJob + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsImageService.getRandomCatImage() }

                val fact = factDeferred.await()
                val images = imageDeferred.await()

                val imageUrl = images.firstOrNull()?.url
                val catPresentation = CatPresentation(
                    fact = fact.fact,
                    imageUrl = imageUrl ?: ""
                )
                _catsView?.populate(catPresentation)
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                _catsView?.showError(e)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showError(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterJob.cancel()
    }
}