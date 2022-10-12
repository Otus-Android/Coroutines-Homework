package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

private const val TAG = "CatsPresenter"

class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope: PresenterScope by lazy { PresenterScope() }

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.setLoading(true)
                val responseImage = catsService.getRandomImage()
                val response: Fact =
                    if (Constants.RESERVE_CATS_SERVER) catsService.getCatFactReserve()
                        .toFact() else catsService.getCatFact()
                _catsView?.populate(CatDescription(response.text, responseImage.file))
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
        presenterScope.close()
    }

    private class PresenterScope() : CoroutineScope, AutoCloseable {
        override val coroutineContext: CoroutineContext =
            Dispatchers.Main + CoroutineName("CatsCoroutine")

        override fun close() {
            coroutineContext.cancel()
        }
    }
}