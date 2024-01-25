package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val applicationContext: Context,
    private val catsService: CatsService
) {

    private val coroutineScope = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)

    private var job: Job? = null

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        job = coroutineScope.launch {
            try {
                _catsView?.populate(fact = catsService.getCatFact())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    Toast.makeText(
                        applicationContext,
                        "Не удалось получить ответ от сервера",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    CrashMonitor.trackWarning(e)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }
}