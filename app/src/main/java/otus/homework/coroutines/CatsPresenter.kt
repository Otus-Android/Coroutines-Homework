package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImagesService: CatsImagesService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsImagesService.getCatImages().first() }

                val factWithImage = FactWithImage(
                    fact.await().fact,
                    image.await().url
                )

                _catsView?.populate(factWithImage)
            } catch (e: Exception) {
                when (e) {
                    java.net.SocketTimeoutException() -> {
                        _catsView?.showShortToast(R.string.server_not_responding)
                    }

                    else -> {
                        _catsView?.showShortToast(e.message.toString())
                        CrashMonitor.trackWarning()
                    }
                }
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