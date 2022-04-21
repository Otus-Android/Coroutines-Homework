package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val response = catsService.getCatFact()
                _catsView?.populate(response)
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                _catsView?.showServerResponseError()
            } catch (e: Exception) {
                e.message?.let { errorMessage ->
                    _catsView?.showError(errorMessage)
                }
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}