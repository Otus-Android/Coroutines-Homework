package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private lateinit var job: Job

    fun onInitComplete() {
        job = PresenterScope().launch {
            try {
                val fact = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                _catsView?.populate(fact)
            } catch (t: SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервера")
            } catch (t: Throwable) {
                t.message?.let { _catsView?.showError(it) }
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancel()
        _catsView = null
    }

}

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()

}