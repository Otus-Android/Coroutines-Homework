package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(CoroutineName("CatsCoroutine") + Job() + Dispatchers.Main)
    private var job: Job = Job()

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                _catsView?.populate(result)
            } catch (e: Exception) {
                when(e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервера")
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showError(e.message ?: "")
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}