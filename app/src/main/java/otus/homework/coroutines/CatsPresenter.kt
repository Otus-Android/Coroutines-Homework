package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        scope.launch {
            try {
                val fact = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val image = withContext(Dispatchers.IO) {
                    imagesService.getCatImage()
                }
                _catsView?.populate(Content(fact = fact, image = image))
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                val message = e.message.orEmpty()
                CrashMonitor.trackWarning(message)
                _catsView?.showToast(message)
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
