package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
}

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = PresenterScope().launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            println(throwable)
        }) {
            try {
                val response = catsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    _catsView?.populate(response.body()!!)
                }
            } catch (ex: SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервера")
            } catch (ex: Exception) {
                if (ex !is CancellationException) {
                    CrashMonitor.trackWarning()
                    ex.message?.let { _catsView?.showError(it) }
                }
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