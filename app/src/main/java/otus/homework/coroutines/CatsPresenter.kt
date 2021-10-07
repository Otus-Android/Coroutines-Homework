package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val coroutineScope: CoroutineScope
) {
    private val TAG = "CatsPresenter"

    private var _catsView: ICatsView? = null
    private var presenterJob: Job? = null

    fun onInitComplete() {
        val exceptionHandler = createExceptionHandler()
        presenterJob = coroutineScope.launch(exceptionHandler) {
            val factResponse = withContext(Dispatchers.IO) {
                catsService.getCatFact()
            }
            _catsView?.populate(factResponse)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelCurrentJob() {
        Log.d(TAG, "cancelCurrentJob: is called")
        presenterJob?.cancel()
    }

    private fun createExceptionHandler(): CoroutineExceptionHandler {
        return coroutineScope.createExceptionHandler {
            onFailure(it)
        }
    }

    private fun onFailure(throwable: Throwable) {
        if (throwable is SocketTimeoutException) {
            _catsView?.showToast("Не удалось получить ответ от сервера")
        } else {
            CrashMonitor.trackWarning()
            _catsView?.showToast(throwable.message ?: "Unknown problem")
        }
    }

}

inline fun CoroutineScope.createExceptionHandler(
    crossinline action: (throwable: Throwable) -> Unit
) = CoroutineExceptionHandler { _, throwable ->
    launch {
        action(throwable)
    }
}