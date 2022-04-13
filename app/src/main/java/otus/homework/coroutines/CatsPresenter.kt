package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val coroutineScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null
    private var _initJob: Job? = null

    fun onInitComplete() {
        _initJob = coroutineScope.launch {
            getFact()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _initJob?.cancel()
        _catsView = null
    }

    private suspend fun getFact() {
        runCatching { catsService.getCatFact() }
            .onSuccess { _catsView?.populate(it) }
            .onFailure(::handleGetFactErrors)
    }

    private fun handleGetFactErrors(throwable: Throwable) {
        checkCancellationException(throwable)

        if (throwable is SocketTimeoutException) {
            _catsView?.showError(ICatsView.Error.ServerConnectionError)
            return
        }

        CrashMonitor.trackWarning()
        throwable.message?.let { message ->
            _catsView?.showError(ICatsView.Error.UnknownError(message))
        }
    }

    private fun checkCancellationException(throwable: Throwable) {
        if (throwable is CancellationException) {
            throw throwable
        }
    }

}