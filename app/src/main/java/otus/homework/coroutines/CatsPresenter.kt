package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val scope = PresenterScope()
    private var _catsView: ICatsView? = null

    private var catsServiceJob: Job? = null

    fun onInitComplete() {
        catsServiceJob?.cancel()
        catsServiceJob = scope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)

            } catch (e: SocketTimeoutException) {
                CrashMonitor.trackWarning()
                _catsView?.showError("Не удалось получить ответ от сервером")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catsServiceJob?.cancel()
        _catsView = null
    }
}