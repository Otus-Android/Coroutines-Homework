package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val presenterScope: CoroutineScope,
    private val dispatcherIo: CoroutineDispatcher
) {

    private var _catsView: ICatsView? = null

    private fun <T> catResponseAsync(
        scope: CoroutineScope,
        responseBlock: suspend () -> Response<T>
    ): Deferred<T?> = scope.async(dispatcherIo) {
        try {
            val response = responseBlock()
            if (response.isSuccessful) {
                response.body()?.let { return@async it }
                    ?: CrashMonitor.trackWarning(message = "Empty response body")
            } else {
                val crashMessage = response.errorBody().toString()
                CrashMonitor.trackWarning(message = crashMessage)
            }
            return@async null
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
                    e.message?.let { _catsView?.showToast(it) }
                }
            }
            return@async null
        }
    }

    fun onInitComplete() {
        presenterScope.launch {
            val deferredFact = catResponseAsync(this) { catsService.getCatFact() }
            val deferredImageUrl = catResponseAsync(this) { catsImageService.getImageUrl() }
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