package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        presenterScope.launch(errorHandler) {
            try {
                val factJob = async { catsService.getCatFact() }
                val imageJob = async { catsService.getCatImage() }

                _catsView?.populate(
                    FactAndImage(
                        factJob.await(),
                        imageJob.await()
                    )
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.onError("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        e.message?.let { _catsView?.onError(it) }
                        CrashMonitor.trackWarning()
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}