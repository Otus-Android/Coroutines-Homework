package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = presenterScope.async {
            val image = async { imageService.getCatImage().first() }
            val fact = async { catsService.getCatFact() }

            _catsView?.populate(CatsUIState(fact.await(), image.await()))
        }
        presenterScope.launch {
            try {
                (job as Deferred<*>).await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                e.message?.let(::showToast)
            }
        }
    }

    private fun showToast(message: String) {
        _catsView?.showToast(message)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelCoroutine() {
        job?.run {
            if (isActive) {
                cancel()
            }
        }
    }
}