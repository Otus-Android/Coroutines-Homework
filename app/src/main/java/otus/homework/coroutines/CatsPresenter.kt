package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException

const val TAG = "CatsPresenter"

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val baseScope = PresenterScope()

    fun onInitComplete() {
        runBlocking(Dispatchers.IO) {
            baseScope.launch {
                try {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        _catsView?.populate(response.body()!!)
                    }
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> {
                            _catsView?.showToast("Не удалось получить ответ от сервером")
                        }
                        else -> {
                            CrashMonitor.trackWarning(e.message.toString())
                            _catsView?.showToast(e.message.toString())
                        }
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

    fun onDestroy() {
        baseScope.cancel()
    }
}