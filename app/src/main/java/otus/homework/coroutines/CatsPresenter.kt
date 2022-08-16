package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.api.ImagesService
import otus.homework.coroutines.models.Content
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable.message.orEmpty())
        }) {
            try {
                val fact = async {
                    catsService.getCatFact()
                }
                val image = async {
                    imagesService.getCatImage()
                }
                _catsView?.populate(
                    Content(
                        fact = fact.await(),
                        image = image.await()
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
