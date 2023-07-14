package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import otus.homework.coroutines.model.CatModel
import java.net.SocketTimeoutException

class CatsPresenter(
    private val factService: CatsFactService,
    private val imageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val catsScope = PresenterScope()

    fun onInitComplete() {
        runBlocking(Dispatchers.IO) {
            catsScope.launch {
                try {
                    val catFactJob = async { factService.getCatFact() }
                    val catImageJob = async { imageService.getCatImage() }

                    val catModel = CatModel(
                        catFactJob.await().body()?.fact,
                        CatsImageService.BASE_URL + catImageJob.await().body()?.url)

                    _catsView?.populate(catModel)
                } catch (e: Exception) {
                    when (e) {
                        is SocketTimeoutException -> {
                            _catsView?.showToast(R.string.error_connection)
                        }
                        is CancellationException ->  {
                            throw e
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

    fun onStop(){
        catsScope.cancel()
    }
}
