package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope: PresenterScope =
        PresenterScope(Dispatchers.Main, CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.populate(
                    FactAndImage(
                        catsService.getCatFact(),
                        catsService.getCatImage()
                    )
                )
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.onError("Не удалось получить ответ от сервера")
                    }
                    else -> {
                        e.message?.let { _catsView?.onError(it) }
                        CrashMonitor.trackWarning()
                    }
                }
            }

            catsService.getCatFact()
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