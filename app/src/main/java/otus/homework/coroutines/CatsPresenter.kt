package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Deprecated("Changed to ViewModel")
class CatsPresenter(
    private val catsService: CatsService,
    private val catsImagesService: CatsImagesService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch(CoroutineExceptionHandler { _, throwable ->
            _catsView?.showShortToast(throwable.message.toString())
            CrashMonitor.trackWarning()
        }) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsImagesService.getCatImages().first() }

                val factWithImage = FactWithImage(
                    fact.await().fact,
                    image.await().url
                )

                _catsView?.populate(factWithImage)
            } catch (e: SocketTimeoutException) {
                _catsView?.showShortToast(R.string.server_not_responding)
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