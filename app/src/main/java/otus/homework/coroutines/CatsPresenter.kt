package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                coroutineScope {
                    val catFact = async { catsService.getCatFact() }
                    val catPicture = async { catsService.getCatPicture() }

                    _catsView?.populate(CatsViewUiData(catFact.await(), catPicture.await()))
                }

            } catch (t: Throwable) {
                when (t) {
                    is java.net.SocketTimeoutException -> {
                        _catsView?.makeToast("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        // don't we need to filter if it was coroutine's CancellationException?
                        // otherwise we could fill up analytics with cancellations as crashes
                        CrashMonitor.trackWarning()
                        t.message?.let {
                            _catsView?.makeToast(it)
                        }
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

    private class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}
