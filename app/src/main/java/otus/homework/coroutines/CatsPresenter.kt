package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

private const val TAG = "CatsPresenter"

class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope = PresenterScope()

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        Log.d(TAG, "PresenterScope job: ${presenterScope.coroutineContext[Job]}")

        presenterScope.launch {
            try {
                _catsView?.setLoading(true)
                supervisorScope {
                    val deferredImage: Deferred<ImageDescription> = async {
                        catsService.getRandomImage()
                    }
                    val deferredFact: Deferred<Fact> = async {
                        if (Constants.RESERVE_CATS_SERVER) catsService.getCatFactReserve()
                            .toFact() else catsService.getCatFact()
                    }
                    _catsView?.populate(CatDescription(
                        deferredFact.await().text,
                        deferredImage.await().file
                    ))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "We got a timeout exception", e)
                _catsView?.showError(R.string.error_network)
            } catch (e: Throwable) {
                Log.d(TAG, "We got an unknown exception", e)
                e.message.takeUnless { it.isNullOrEmpty() }?.let {
                    _catsView?.showError(it)
                } ?: run {
                    _catsView?.showError(R.string.error_unknown)
                }
                CrashMonitor.trackWarning()
            } finally {
                _catsView?.setLoading(false)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null

        try {
            presenterScope.cancel()
        } catch (ex: Exception) {
            Log.d(TAG, "We got an exception during canceling the scope", ex)
            throw ex
        }
    }

    private class PresenterScope() : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
    }
}