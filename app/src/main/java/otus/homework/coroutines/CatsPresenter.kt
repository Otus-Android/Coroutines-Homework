package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
        _catsView?.showToast(exception.message.toString())
    }

    fun onInitComplete() {
        presenterScope.launch(handler) {
            try {
                val factAsync = async { catsService.getCatFact() }
                val imageAsync = async { imageService.getImage() }
                _catsView?.populate(
                    CatData(
                        imageAsync.await().url,
                        factAsync.await().fact
                    )
                )
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast(R.string.error_message)
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

}