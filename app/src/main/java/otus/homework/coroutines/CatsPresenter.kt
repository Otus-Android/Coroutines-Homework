package otus.homework.coroutines

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _: CoroutineContext, throwable: Throwable ->
            CrashMonitor.trackWarning()
        }

    fun onInitComplete() {
        presenterScope.launch(coroutineExceptionHandler) {
            try {
                coroutineScope {
                    val catFact = async { catsService.getCatFact() }
                    val catPicture = async { catsService.getCatPicture() }

                    _catsView?.populate(CatsViewUiData(catFact.await(), catPicture.await()))
                }
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.makeToast("Couldn't fetch response from the server")
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

    private class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}
