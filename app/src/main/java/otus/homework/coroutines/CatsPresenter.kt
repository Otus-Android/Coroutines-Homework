package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope: CoroutineScope = PresenterScope
    private lateinit var job: Job
    fun onInitComplete() {
           job = presenterScope.launch {
                try {
                    val response = withContext(Dispatchers.IO){ // можно и на Main - retrofit все равно переключит
                        catsService.getCatFact()
                    }
                    _catsView?.populate(response)
                } catch (e:  SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")

                } catch (e: Exception){
                    _catsView?.showToast(e.message.toString())
                    CrashMonitor.trackWarning()
                }
            }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
      //  presenterScope.cancel() // - если хотим отменить все корутины скоупа
        job.cancel() // - отменяем конкретную корутину
    }
}