package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main) + CoroutineName("CatsCoroutine") + job

    fun onInitComplete() {

        scope.launch {
            try {
                val result = catsService.getCatFact()

                if (result.isSuccessful && result.body() != null) {

                    _catsView?.populate(result.body()!!)
                }

            } catch (e: SocketTimeoutException) {
                _catsView?.showError(R.string.timeout_error)
            } catch (e: Throwable) {
                CrashMonitor.trackWarning(e.message)

                _catsView?.showError(e.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancelChildren()

        _catsView = null
    }
}
