package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val catsScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )


     @OptIn(ExperimentalCoroutinesApi::class)
     fun onInitComplete() {
         catsScope.launch {
             try {
                 val fact = async {
                     catsService.getCatFact()
                 }
                 awaitAll(fact)
                 _catsView?.populate(fact.getCompleted())
             } catch (e: SocketTimeoutException) {
               _catsView?.toast("Не удалось получить ответ от сервером")
             } catch (e: Exception) {
                 val msg = e.message ?: "unknown exception"
                 CrashMonitor.trackWarning(msg)
                 _catsView?.toast(msg)
                 throw e
             }
         }
     }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catsScope.cancel()
        _catsView = null
    }
}