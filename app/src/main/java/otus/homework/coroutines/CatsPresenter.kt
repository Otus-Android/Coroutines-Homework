package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private var scope: CoroutineScope? = null

    fun onInitComplete() {
        scope?.launch {
            getCatFact()?.let { fact ->
                _catsView?.populate(fact)
            }
        }
    }

    private suspend fun getCatFact() =
        try {
            loadCatFact()
        } catch (e: SocketTimeoutException) {
            _catsView?.showToast(R.string.cat_fact_server_error)
            null
        } catch (e: Exception) {
            CrashMonitor.trackWarning()
            e.message?.let {
                _catsView?.showToast(it)
            }
            null
        }

    private suspend fun loadCatFact() = coroutineScope {
        catsService.getCatFact()
    }

    fun attachView(catsView: ICatsView) {
        scope = PresenterScope()
        _catsView = catsView
    }

    fun detachView() {
        scope?.cancel()
        scope = null
        _catsView = null
    }
}