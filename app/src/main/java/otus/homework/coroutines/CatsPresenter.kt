package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var presenterScope: CoroutineScope = PresenterScope
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factResponse =
                    withContext(Dispatchers.Default) { catsService.getCatFact() }
                _catsView?.populate(factResponse)

            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsView?.showToastMessage(R.string.socket_timeout_message.toString())
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showToastMessage(ex.message ?: "")
                    }
                }
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