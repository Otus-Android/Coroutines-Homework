package otus.homework.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope by lazy { PresenterScope() }
    fun onInitComplete() {
        presenterScope.launch() {
            try {
                val meme = catsService.getCatFact()
                if (meme.isSuccessful && meme.body() != null)
                    _catsView?.populate(meme.body()!!)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.socketExceptionMessage()
                    }
                    else -> {
                        _catsView?.baseExceptionMessage(e.message.toString())
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
        presenterScope.cancel()
    }
}