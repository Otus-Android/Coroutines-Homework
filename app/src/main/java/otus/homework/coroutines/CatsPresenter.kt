package otus.homework.coroutines

import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private val exHandler = CoroutineExceptionHandler { _, ex ->
        CrashMonitor.trackWarning(ex)
    }

    fun onInitComplete() {
        presenterScope.launch(exHandler + SupervisorJob()) {
            val catFactDef = async(Dispatchers.IO) { catsService.getCatFact() }
            val catImgDef = async(Dispatchers.IO) { catsImageService.getCatImage() }
            try {
                _catsView?.populate(CatModel(catFactDef.await(), catImgDef.await()))
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsView?.showSocketExceptionMessage()
                    }
                    is CancellationException -> {
                        throw ex
                    }
                    else -> {
                        _catsView?.showExceptionMessage(ex.message.toString())
                    }
                }

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

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }


}