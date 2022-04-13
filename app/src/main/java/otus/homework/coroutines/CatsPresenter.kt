package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val coroutineScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null
    private var _refreshJob: Job? = null

    fun onInitComplete() {
        _refreshJob = coroutineScope.launch {
            val getFactDeferred = async { getFact() }
            val getImageDeferred = async { getRandomImage() }

            val fact = getFactDeferred.await()
            val image = getImageDeferred.await()

            val catsViewState = CatsViewState(
                fact = fact,
                image = image
            )
            _catsView?.populate(catsViewState)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _refreshJob?.cancel()
        _catsView = null
    }

    private suspend fun getFact() =
        runCatching { catsService.getCatFact() }
            .onFailure(::handleServerErrors)
            .getOrNull()

    private suspend fun getRandomImage() =
        runCatching { catsService.getRandomImage() }
            .onFailure(::handleServerErrors)
            .getOrNull()

    private fun handleServerErrors(throwable: Throwable) {
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