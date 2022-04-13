package otus.homework.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val catsPictureService: CatsPictureService
) {

    private val presenterScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext = Dispatchers.Main +
                CoroutineName("CatsCoroutine") +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    if (throwable is java.net.SocketTimeoutException) {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    } else {
                        CrashMonitor.trackWarning()
                        _catsView?.showToast(throwable.message ?: "")
                    }
                }
    }

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            val fact = async {
                catsService.getCatFact()
            }
            val pict = async {
                catsPictureService.get()
            }
            _catsView?.populate(Cat(fact.await(), pict.await().file))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        if (presenterScope.isActive) presenterScope.cancel()
    }
}