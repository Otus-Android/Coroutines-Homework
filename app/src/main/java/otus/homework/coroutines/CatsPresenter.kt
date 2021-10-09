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
            val image = catsService.getCatImage()

            _catsView?.populate(Cat(fact, image))
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