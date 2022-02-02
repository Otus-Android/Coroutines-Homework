package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val service: CatsService,
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val fact = async(Dispatchers.IO) { service.getCatFact() }
                val image = async(Dispatchers.IO) { service.getCatImage() }

                _catsView?.populate((CatsInfo(fact.await().text, image.await().file)))
                Log.d("TEST1", "fact.await().text ${fact.await().text}")
            } catch (e: SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e.toString())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
    }
}