package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

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
            val factDeferred =
                async {
                    try {
                        catsService.getCatFact(FACT_URI)
                    } catch (e: Exception) {
                        when (e) {
                            SocketTimeoutException() ->
                                _catsView?.showToast("Не удалось получить ответ от сервера")
                            else -> {
                                CrashMonitor.trackWarning()
                                e.message?.let { _catsView?.showToast(it) }
                            }
                        }
                        throw CancellationException(e.message)
                    }
                }

            val imageUriDeferred =
                async {
                    try {
                        catsImageService.getCatImage(IMAGE_URI)
                    } catch (e: Exception) {
                        when (e) {
                            SocketTimeoutException() ->
                                _catsView?.showToast("Не удалось получить ответ от сервера")
                            else -> {
                                CrashMonitor.trackWarning()
                                e.message?.let { _catsView?.showToast(it) }
                            }
                        }
                        throw CancellationException(e.message)
                    }
                }
            _catsView?.populate(factDeferred.await(), imageUriDeferred.await())
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    companion object {
        private const val FACT_URI = "https://cat-fact.herokuapp.com/facts/random?animal_type=cat"
        private const val IMAGE_URI = "https://aws.random.cat/meow"
    }
}