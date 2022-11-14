package otus.homework.coroutines

import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.toString())
        (_catsView as CatsView).showToast("Не удалось получить ответ от сервера $exception")
    }

    private val catsScope =
        CoroutineScope(defaultDispatcher + CoroutineName("CatsCoroutine") + handler)

    private suspend fun getCatFact(): TextFact = catsService.getCatFact()
    private suspend fun getCatImage(): ImageFact = catsImageService.getCatImage()

    fun onInitComplete() {

        catsScope.launch {
            try {
                val factImage = getCatImage();
                val factText = getCatFact();
                _catsView?.populate(Fact(factText.text, factImage.file))
            } catch (e: java.net.SocketTimeoutException) {
                (_catsView as CatsView).showToast("Не удалось получить ответ от сервера $e")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        catsScope.cancel()
    }
}