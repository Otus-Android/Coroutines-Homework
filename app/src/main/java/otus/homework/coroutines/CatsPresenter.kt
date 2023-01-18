package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()


    fun onInitComplete() {
        presenterScope.launch{
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (e: SocketTimeoutException) {
                _catsView?.CatsMessage("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.CatsMessage(e.message.toString())
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


class PresenterScope() : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main +
            CoroutineName("CatsCoroutine") +
            Job()
}