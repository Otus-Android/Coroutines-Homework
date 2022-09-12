package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val presenterScope: CoroutineScope,
) {

    private var _catsView: ICatsView? = null

    private suspend fun <T> catResponse(block: suspend () -> T) =
        try {
            block()
        } catch (e: Exception) {
            when (e) {
                is CancellationException -> throw e
                is SocketTimeoutException -> {
                    CrashMonitor.trackWarning(message = e.message, throwable = e)
                    val crashMessage = "Failed to get a response from the server"
                    _catsView?.showToast(text = crashMessage)
                }
                else -> {
                    CrashMonitor.trackWarning(message = e.message, throwable = e)
                    e.message?.let {
                        _catsView?.showToast(it)
                    }
                }
            }
            null
        }

    fun onInitComplete() {
        presenterScope.launch {
            val deferredFact = async { catResponse { catsService.getCatFact() } }
            val deferredImageUrl = async { catResponse { catsImageService.getImageUrl() } }
            val content =
                CatsContent(fact = deferredFact.await(), imageUrl = deferredImageUrl.await())
            _catsView?.populate(content)
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