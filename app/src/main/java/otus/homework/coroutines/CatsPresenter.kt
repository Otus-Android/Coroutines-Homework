package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val catFact = catsService.getCatFact()
                _catsView?.populate(catFact)
            } catch (e: SocketTimeoutException) {
                _catsView?.showMessage("Не удалось получить ответ от сервером")
            } catch (e: Exception) {
                e.message?.let {
                    _catsView?.showMessage(it)
                }
                CrashMonitor.trackWarning(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}