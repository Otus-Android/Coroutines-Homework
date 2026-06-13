package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val factDeferred = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val imageDeferred = async(Dispatchers.IO) {
                    catsImageService.getCatImage()
                }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                val presentationModel = PresentationModel(fact = fact.fact, imageUrl = image[0].url)
                _catsView?.populate(presentationModel)

            } catch (_: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                _catsView?.showToast(e.message ?: "")
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
}

class PresenterScope() : CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext =
        Dispatchers.Main + job + CoroutineName("CatsCoroutine")

    fun cancel() {
        job.cancel()
    }
}