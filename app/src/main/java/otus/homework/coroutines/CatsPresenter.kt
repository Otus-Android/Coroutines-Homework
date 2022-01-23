package otus.homework.coroutines

import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService,

) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private lateinit var getFactJob: Job

    fun onInitComplete() {
        getFactJob = presenterScope.launch {

            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (ex: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (ex: Exception) {
                CrashMonitor.trackWarning(ex)
                _catsView?.showToast(ex.message ?: "")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStopWork() {
        getFactJob.cancel()
    }
}