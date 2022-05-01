package otus.homework.coroutines

import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val cats = async (Dispatchers.IO) { catsService.getCatFact() }
                _catsView?.populate(cats.await())
            } catch (e: SocketTimeoutException) {
                _catsView?.showErrorMessage("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                e.message?.let { _catsView?.showErrorMessage(it) }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() = scope.cancel()
}

class PresenterScope: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get () = SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
}