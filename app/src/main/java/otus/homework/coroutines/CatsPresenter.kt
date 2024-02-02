package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope =
        CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main.immediate)

    private var job: Job? = null

    fun onInitComplete() {

        job = presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)

            } catch (e1: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showToast(e.toString())
                CrashMonitor.trackWarning()
            }
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }
}