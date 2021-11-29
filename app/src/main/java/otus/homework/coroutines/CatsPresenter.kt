package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val coroutineScope: HomeWorkScope
) {

    private val coroutineException = CoroutineExceptionHandler { coroutineContext, throwable ->
        CrashMonitor.trackWarning(throwable, CatsPresenter.TAG)
    }

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch(coroutineException) {
            supervisorScope {
                try {
                    val fact = async(Dispatchers.IO) { catsService.getCatFact() }
                    val img = async(Dispatchers.IO) {catsService.getCatimg("https://aws.random.cat/meow") }
                    _catsView?.populate(FullFact(fact.await(), img.await()))
                } catch (e: CancellationException) {
                    Log.e("CoroutineExaption", e.toString())
                    throw e
                } catch (e: java.net.SocketTimeoutException) {
                    Log.e("Socket", "oshibka")
                    _catsView?.callOnErrorSocketException()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        coroutineScope.cancel()
    }

    companion object {
        val TAG = "CatsPresenter"
    }
}