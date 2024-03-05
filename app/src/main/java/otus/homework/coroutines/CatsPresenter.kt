package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService

) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()
    private var job: Job? = null

    fun onStop() {
        if (job?.isActive == true) {
            job?.cancel()
        }
    }

    fun onInitComplete() {

        job = scope.launch(CoroutineExceptionHandler { _, throwable ->
            _catsView?.showToast(throwable.message.toString())
            CrashMonitor.trackWarning()
        }) {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { imageService.getImage().first() }

                val factWithImage = FactWithImage(
                    fact.await().fact,
                    image.await().url
                )

                _catsView?.populate(factWithImage)
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showToast(R.string.socket_timeout_exception_message)
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