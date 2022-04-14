package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val crashMonitor: CrashAnalyticManager,
    private val catsService: CatsService,
    private val scope: CoroutineScope
) {
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch {
            getFact()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun onStop() {
        cancelJobs()
    }

    fun detachView() {
        cancelJobs()
        _catsView = null
    }

    private suspend fun getFact() {
        exceptionHandler {
            val fact = catsService.getCatFact()
            _catsView?.populate(fact)
        }
    }

    private suspend fun exceptionHandler(callback: suspend () -> Unit) {
        try {
            callback.invoke()
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: SocketTimeoutException) {
            _catsView?.showServerError()
        } catch (exception: Exception) {
            crashMonitor.trackWarning()
            exception.message?.let {
                _catsView?.showDefaultError(message = it)
            }
        }
    }

    private fun cancelJobs() {
        scope.coroutineContext[Job]?.children?.forEach {
            it.cancel()
        }
    }
}