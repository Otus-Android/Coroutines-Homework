package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

private val catsPresenterCoroutineScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

class CatsPresenter(
    private val catsService: CatsService,
    private val crashMonitor: CrashMonitor,
) {
    private val presenterScope: CoroutineScope = catsPresenterCoroutineScope
    private var _catsView: ICatsView? = null
    private var factRequestJob: Job? = null

    fun onInitComplete() {
        if (factRequestJob?.isActive == true) {
            crashMonitor.trackWarning("the request is already being made")
            return
        }
        factRequestJob = presenterScope.launch { requestFact() }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        factRequestJob?.cancel()
        _catsView = null
    }

    private suspend fun requestFact() {
        try {
            val facts = withContext(Dispatchers.IO) {
                catsService.getCatFact()
            }
            _catsView?.populate(facts)
        } catch (ex: SocketTimeoutException) {
            crashMonitor.trackError("error getting facts", ex)
            _catsView?.showError("Не удалось получить ответ от сервера")
        } catch (ex: Throwable) {
            crashMonitor.trackError("error getting facts", ex)
            _catsView?.showError(ex.message ?: "Произошла неизвестная ошибка")
        }
    }
}