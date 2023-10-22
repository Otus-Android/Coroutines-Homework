package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val job = Job()
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + job)

    fun onInitComplete() {
       presenterScope.launch {
           try {
               val fact = catsService.getCatFact()
               _catsView?.populate(fact)
           } catch (e: SocketTimeoutException) {
               _catsView?.showExceptionMessage("Не удалось получить ответ от сервера.")
           } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
           }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelAllJobs() {
        presenterScope.cancel()
    }
}