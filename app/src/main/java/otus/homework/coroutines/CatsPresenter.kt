package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(
        context = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = withContext(Dispatchers.IO) { catsService.getCatFact() }
                _catsView?.populate(fact)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        throw e
                    }
                    is SocketTimeoutException -> {
                        Log.e(TAG, "Не удалось получить ответ от сервера", e)
                        _catsView?.showToast("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        Log.e(TAG, "Что-то пошло не так", e)
                        CrashMonitor.trackWarning()
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
    }

    fun onStop() {
        presenterScope.coroutineContext.cancelChildren()
    }

    companion object {
        private const val TAG = "CatsPresenter"
    }
}