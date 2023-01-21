package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val meowService: MeowService,
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(
        context = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val vo = withContext(Dispatchers.IO) {
                    val factDeferred = async { catsService.getCatFact() }
                    val meowDeferred = async { meowService.getCatImage() }
                    CatsVO(
                        fact = factDeferred.await().fact,
                        imageUrl = meowDeferred.await().imageUrl
                    )
                }
                _catsView?.populate(vo)
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