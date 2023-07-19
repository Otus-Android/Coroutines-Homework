package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsFactService,
    private val imageService: CatsImageService
) {

    private val scope = PresenterScope()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning()
        throwable.message?.let { _catsView?.showToast(it) }
    }

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch(exceptionHandler) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { imageService.getCatImage().first() }

                _catsView?.populate(
                    CatModel(
                        fact.await(),
                        image.await()
                    )
                )
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}
