package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
}

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private var getCatFact: Job? = null
    fun onInitComplete(context: Context) {
        val scope = PresenterScope()
        getCatFact = scope.launch {
            try {
                _catsView?.populate(catsService.getCatFact())
            } catch (e: Exception) {
                when (e) {
                    is java.net.SocketTimeoutException -> {
                        Toast.makeText(
                            context,
                            "Не удалось получить ответ от сервера",
                            Toast.LENGTH_SHORT
                        )
                    }
                    else -> {
                        CrashMonitor.trackWarning(e.message)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        getCatFact?.cancel("App closed")
        _catsView = null
    }
}