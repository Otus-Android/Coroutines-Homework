package otus.homework.coroutines

import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            supervisorScope {
                val facts = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                try {
                    _catsView?.populate(facts.await())
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showToast(R.string.socket_error)
                } catch (ex: Throwable) {
                    CrashMonitor.trackWarning()
                    if (ex.message != null) {
                        _catsView?.showToast(ex.message!!)
                    } else {
                        _catsView?.showToast(R.string.general_error)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.cancel()
        _catsView = null
    }
}