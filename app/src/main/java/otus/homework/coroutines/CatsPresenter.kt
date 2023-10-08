package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName(name = "CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервером")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.message?.let { msg ->_catsView?.showToast(msg) }
                CrashMonitor.trackWarning()
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
        presenterScope.cancel()
    }
}