package otus.homework.coroutines

import com.example.namespace.R
import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val serviceRandomCatFact: CatsService,
    private val serviceRandomCatImage: CatsService
) {

    private val scope = PresenterScope()
    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = scope.launch {
            try {
                val fact = async { serviceRandomCatFact.getCatFact() }
                val image = async { serviceRandomCatImage.catImage() }
                val catItem = CatItem(fact.await().fact, image.await().file)
                _catsView?.populate(catItem)
            } catch (e: SocketTimeoutException) {
                _catsView?.message(R.string.failed_response_server)
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                _catsView?.message(e.message.toString())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }
}

class PresenterScope : CoroutineScope {
    override val coroutineContext = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
}
