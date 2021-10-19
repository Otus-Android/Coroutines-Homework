package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) : HomeWorkScope() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        launch(Dispatchers.IO) {
            try {
                val fact =  catsService.getCatFact()
                val img = catsService.getCatimg("https://aws.random.cat/meow")
                _catsView?.populate(FullFact(fact,img))
            } catch (e: CancellationException) {
                Log.d("CoroutineExaption", e.toString())
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.callOnErrorSocketException()
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e, TAG)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        coroutineContext.cancel()
    }

    companion object {
        val TAG = "CatsPresenter"
    }
}