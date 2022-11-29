package otus.homework.coroutines

import kotlinx.coroutines.*
import okhttp3.internal.wait

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val catsScope: CoroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )


     fun onInitComplete() {

         val handler = CoroutineExceptionHandler { _, exception ->
             val msg = exception.message ?: "unknown exception"
             CrashMonitor.trackWarning(msg)
             _catsView?.toast(msg)
         }
         catsScope.launch(SupervisorJob() + handler) {
             val fact = async {
                 catsService.getCatFact()
             }
             val img = async {
                 catsService.getCatImage()
             }
             try {
                 listOf(fact, img).awaitAll()
                 _catsView?.populate(fact.getCompleted())
                 _catsView?.populate(img.getCompleted())
             } catch (e: java.net.SocketTimeoutException) {
                 _catsView?.toast("Не удалось получить ответ от сервером")
             }
         }
     }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catsScope.cancel("detach view")
        _catsView = null
    }
}