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
            val fact = async(Dispatchers.IO) { catsService.getCatFact() }
            val imageUrl = async(Dispatchers.IO) { imageService.getCatImage() }
            try {
                _catsView?.populate(CatsPresentation(fact.await(), imageUrl.await().file))
            } catch (ex: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
                throw CancellationException()
            } catch (ex: Exception) {
                CrashMonitor.trackWarning(ex)
                _catsView?.showToast(ex.message ?: "")
                throw CancellationException()
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