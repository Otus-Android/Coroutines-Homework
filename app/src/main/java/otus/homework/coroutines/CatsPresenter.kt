package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val presenterScope: CoroutineScope,
    private val dispatcherIo: CoroutineDispatcher
) {

    private var _catsView: ICatsView? = null

    private fun <T> CoroutineScope.catResponseAsync(block: suspend () -> T) =
        async(dispatcherIo) {
            try {
                return@async block()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> {
                        CrashMonitor.trackWarning(message = e.message, throwable = e)
                        val crashMessage = "Failed to get a response from the server"
                        withContext(Dispatchers.Main) {
                            _catsView?.showToast(text = crashMessage)
                        }
                    }
                    else -> {
                        CrashMonitor.trackWarning(message = e.message, throwable = e)
                        e.message?.let {
                            withContext(Dispatchers.Main) {
                                _catsView?.showToast(it)
                            }
                        }
                    }
                }
                return@async null
            }
        }

    fun onInitComplete() {
        presenterScope.launch {
            val deferredFact = catResponseAsync { catsService.getCatFact() }
            val deferredImageUrl = catResponseAsync { catsImageService.getImageUrl() }
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