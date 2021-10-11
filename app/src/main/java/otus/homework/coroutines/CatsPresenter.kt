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
            val catFact = try {
                catsService.getCatFact()
            } catch (t: Throwable) {
                when (t) {
                    is java.net.SocketTimeoutException -> {
                        _catsView?.makeToast("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        t.message?.let {
                            _catsView?.makeToast(it)
                        }
                    }
                }
                null
            }

            if (catFact != null) {
                _catsView?.populate(catFact)
            }
            delay(3000)
            _catsView?.makeToast("Babe")
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
