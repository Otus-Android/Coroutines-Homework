package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) {
    private val tag = this.javaClass.simpleName
    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catImageService.getCatImage() }
                val catInfo = CatFactWithImage(fact.await().fact, image.await().url)
                _catsView?.populate(catInfo)
            } catch (e: SocketTimeoutException) {
                _catsView?.showError(R.string.error_failed_to_get_response_from_server)
            } catch (e: Exception) {
                _catsView?.showError(e.message)
                CrashMonitor.trackWarning(tag, e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.cancel()
        _catsView = null
    }
}