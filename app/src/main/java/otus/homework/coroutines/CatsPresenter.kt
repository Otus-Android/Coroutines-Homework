package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) {
    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private var currentJob: Job? = null

    fun onInitComplete() {
        currentJob?.cancel()
        currentJob = presenterScope.launch {
            try {
                val factDeferred = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageDeferred = async(Dispatchers.IO) { catImageService.getRandomCatImage() }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                val presentationData = CatPresentationData(
                    fact = fact.fact,
                    imageUrl = image.file
                )

                _catsView?.populate(presentationData)
            } catch (e: SocketTimeoutException) {
                handleError("Не удалось получить ответ от сервером", e)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                handleError(e.message ?: "Произошла ошибка", e)
            }
        }
    }

    private fun handleError(message: String, exception: Exception) {
        presenterScope.launch {
            _catsView?.showError(message)
            CrashMonitor.logException(exception)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        currentJob?.cancel()
        currentJob = null
    }

    fun onDestroy() {
        presenterScope.cancel()
    }
}

class PresenterScope : CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext = job + Dispatchers.Main + CoroutineName("CatsCoroutine")

    fun cancel() {
        job.cancel()
    }
}