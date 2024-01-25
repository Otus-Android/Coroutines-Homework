package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            _catsView?.populate(response.body()!!)
                        }
                    }
                }
            } catch (t: Throwable) {
                if (t is SocketTimeoutException) {
                    _catsView?.showNetworkError()
                } else {
                    _catsView?.showError(t)
                    CrashMonitor.trackWarning()
                }
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
        coroutineScope.cancel()
    }
}