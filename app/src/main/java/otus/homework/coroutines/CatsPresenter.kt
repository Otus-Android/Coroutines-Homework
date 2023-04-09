package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()
    private lateinit var job: Job

    private val handler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SocketTimeoutException -> {
                _catsView?.showToast(R.string.error_message)
            }
            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showToast(exception.message.toString())
            }
        }
    }

    fun onInitComplete() {
        cancelJob()
        job = presenterScope.launch(handler) {
            /*
            * Подскажи, пожалуйста, что почитать про exception handling.
            * Например, если картинки упали с SocketTimeoutException,
            * то я хочу показать только текст без картинки.
            * */
            ensureActive()
            val factAsync = async { catsService.getCatFact() }
            val imageAsync = async { imageService.getImage() }
            _catsView?.populate(
                CatData(
                    imageAsync.await().url,
                    factAsync.await().fact
                )
            )
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        cancelJob()
        presenterScope.cancel()
    }

    private fun cancelJob() {
        /*
        * Везде пишут, что нужно кэнселить скоуп.
        * Но если я отменяю скоуп, то job остаётся активным (isActive == true).
        * А если отменяю job, то isActive == false.
        * Что я делаю не так?
        * */
        if (::job.isInitialized) {
            job.cancel()
        }
    }

}