package otus.homework.coroutines

import kotlinx.coroutines.*

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {
    private val presenterScope: CoroutineScope =
        CoroutineScope(Job() + Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        _catsView?.hideViews()
        _catsView?.showProgressBar(true)
        presenterScope.launch {
            try {
                val fact = catsService.getCatFact(FACT_URI)
                val imageUri = catsImageService.getCatImage(IMAGE_URI)
                _catsView?.populate(fact, imageUri)
            } catch (e: Exception) {
                if (e == java.net.SocketTimeoutException()) {
                    _catsView?.showToast("Не удалось получить ответ от сервером")
                } else {
                    CrashMonitor.trackWarning()
                    e.message?.let { _catsView?.showToast(it) }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    companion object{
        private const val FACT_URI = "https://cat-fact.herokuapp.com/facts/random?animal_type=cat"
        private const val IMAGE_URI = "https://aws.random.cat/meow"
    }
}