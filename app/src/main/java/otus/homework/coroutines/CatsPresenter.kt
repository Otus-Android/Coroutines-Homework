package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatData
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
) {

    private var _catsView: ICatsView? = null

    private var job: Job? = null

    fun onInitComplete() {
        try {
            job = PresenterScope().launch {
                val fact = async(Dispatchers.IO) { catsService.getCatFact() }
                val image = async(Dispatchers.IO) { imageService.getCatImage() }
                _catsView?.populate(CatData(image.await().path, fact.await()))
            }
        } catch (e: Exception) {
            onError(e)
        }
    }

    private fun onError(e: Exception) {
        when (e) {
            is SocketTimeoutException -> {
                _catsView?.showMessage(R.string.socket_timeout_error_text)
            }
            else -> {
                CrashMonitor.trackWarning()
                e.message?.let {
                    _catsView?.showMessage(it)
                } ?: _catsView?.showMessage(R.string.unknown_error)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }

    class PresenterScope() : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}