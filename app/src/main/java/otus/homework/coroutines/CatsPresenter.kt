package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var factJob: Job? = null
    private var refreshJob: Job? = null

    fun onInitComplete() {
        presenterScope.launch {
            factJob?.cancelAndJoin()
            factJob = launch {
                try {
                    val result = withContext(Dispatchers.IO) {
                        catsService.getCatFact()
                    }
                    _catsView?.populate(result)
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showErrorDialog(ex.localizedMessage ?: "error")
                }
                catch (ex : Exception) {
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun loadFactAndImage() {
        presenterScope.launch {
            refreshJob?.cancelAndJoin()
            refreshJob = launch {
                val factResult = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageResult = async(Dispatchers.IO) { imageService.getCatImage() }
                try {
                    val factWithImage = factResult.await().copy(image = imageResult.await().fileName)
                    _catsView?.populate(factWithImage)
                } catch (ex: SocketTimeoutException) {
                    _catsView?.showErrorDialog(ex.localizedMessage ?: "error")
                }
                catch (ex : Exception) {
                    CrashMonitor.trackWarning()
                } finally {
                    _catsView?.stopRefreshing()
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