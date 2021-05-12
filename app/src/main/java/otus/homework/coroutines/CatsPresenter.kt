package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.onLoading(true)
                val result = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                if (result.isSuccessful) {
                    _catsView?.populate(Fact(result.body()!!.text))
                } else throw Exception("Not successful result")
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> _catsView?.showErrorDialog(ex.localizedMessage ?: "error")
                    else -> CrashMonitor.trackWarning()
                }
            } finally {
                _catsView?.onLoading(false)
            }
        }
    }

    fun loadFactAndImage() {
        presenterScope.launch {
            try {
                val factResult = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageResult = async(Dispatchers.IO) { imageService.getCatImage() }
                _catsView?.onLoading(true)
                if (factResult.await().isSuccessful && imageResult.await().isSuccessful) {
                    _catsView?.populate(Fact(factResult.await().body()!!.text, imageResult.await().body()!!.fileName))
                } else throw Exception("Not successful result")
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> _catsView?.showErrorDialog(ex.localizedMessage ?: "error")
                    else -> CrashMonitor.trackWarning()
                }
            } finally {
                _catsView?.stopRefreshing()
                _catsView?.onLoading(false)
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