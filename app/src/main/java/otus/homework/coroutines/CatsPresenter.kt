package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null
    private val imageSource = "https://aws.random.cat/meow"

    private val job = Job()
    private val coroutineName = "CatsCoroutine"
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + job + CoroutineName(coroutineName))

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
                if (e is SocketTimeoutException)
                    _catsView?.showToast()
                else {
                    e.message?.let {
                        CrashMonitor.trackWarning(it)
                        _catsView?.showToast(it)
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancel()
        _catsView = null
    }
}