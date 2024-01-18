package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
        _catsView?.showToast(throwable.message)
    }

    fun onInitComplete() {
        scope.launch(exceptionHandler) {
            try {
                coroutineScope {
                    val fact = async { catsService.getCatFact() }
                    val images = async { imagesService.getCatImages() }
                    _catsView?.populate(
                        FactWithImage(
                            fact = fact.await().fact,
                            imageUrl = images.await().firstOrNull()?.url.orEmpty()
                        )
                    )
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.coroutineContext.cancel()
        _catsView = null
    }
}