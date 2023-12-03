package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService
) : CoroutineScope {

    private var job: Job? = null

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        job = launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (e: SocketTimeoutException) {
                _catsView?.failedToResponse()
            } catch (e: CancellationException) {
                CrashMonitor.trackWarning()
                _catsView?.defaultFailed(e)
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
        job = null
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}