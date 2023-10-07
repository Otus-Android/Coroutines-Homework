package otus.homework.coroutines

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
//                    throw java.net.SocketTimeoutException()
                val fact = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                Log.d("TAG", "Fact is $fact")
                _catsView?.populate(fact)
            } catch (sockExcept: java.net.SocketTimeoutException) {
                _catsView?.showErrorToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showErrorToast("${e.message}")
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

    fun cancelCoroutine() {
        presenterScope.coroutineContext.job.cancel()
    }
}