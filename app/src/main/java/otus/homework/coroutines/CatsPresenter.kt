package otus.homework.coroutines

import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private lateinit var getFactJob: Job

    fun onInitComplete() {
        getFactJob = presenterScope.launch {
            try {
                coroutineScope {
                    val fact = async { catsService.getCatFact() }
                    val imageUrl = async { imageService.getCatImage() }
                    _catsView?.populate(CatsPresentation(fact.await(), imageUrl.await().file))
                }
            } catch (ex: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (ex: Exception) {
               if (ex is CancellationException)
                   throw ex
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