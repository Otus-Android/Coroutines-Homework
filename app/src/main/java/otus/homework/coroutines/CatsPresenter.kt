package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null
    private val imageSource = "https://aws.random.cat/meow"

    private val coroutineName = "CatsCoroutine"
    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception.message ?: "ERROR!")
    }
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineName(coroutineName) + handler)

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val imageResponse = async(Dispatchers.IO) {
                    catsService.getCatImage(imageSource)
                }
                val factResponse = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                if (imageResponse.await().body() != null && factResponse.await().body() != null) {
                    val factText = factResponse.await().body()!!.fact
                    val imageUrl = imageResponse.await().body()!!.url
                    _catsView?.populate(CatModel(factText, imageUrl))
                }
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> _catsView?.showToast()
                    else -> e.message?.let {
                        _catsView?.showToast()
                        CrashMonitor.trackWarning(it)
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