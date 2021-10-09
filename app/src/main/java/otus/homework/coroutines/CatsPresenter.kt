package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
) {
    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()
    private var job: Job? = null

    fun onInitComplete() = scope.launch {
        try {
            val fact = catsService.getCatFact()
            _catsView?.populate(fact)
        } catch (exception: Exception) {
            if (exception is SocketTimeoutException) {
                _catsView?.connectionError("Не удалось получить ответ от сервером")
            } else {
                CrashMonitor.trackWarning(exception)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }
}