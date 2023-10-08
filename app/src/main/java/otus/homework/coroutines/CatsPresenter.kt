package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatsService
    ) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {

        job = PresenterScope().launch {
            try {
                val fact = catFactService.getCatFact()
                withContext(Dispatchers.Main) {
                    _catsView?.populate(fact)
                }
            } catch (t: SocketTimeoutException) {
                _catsView?.showToast(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
            } catch (t: Throwable) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(t.message)
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

    companion object {
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"
    }
}

