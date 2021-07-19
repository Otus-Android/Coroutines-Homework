package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    fun onInitComplete() {
        presenterScope.launch {
            try {
                val meme = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
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