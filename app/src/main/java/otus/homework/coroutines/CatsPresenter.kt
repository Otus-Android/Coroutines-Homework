package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private var catsScope =  PresenterScope()

    fun onInitComplete() {
        catsScope.launch {
            try {
                val fact = async { handleResponse(catsService.getCatFact())
                }
                val image = async { handleResponse(imageService.getRandomImage()) }
                _catsView?.populate(CatsModel(fact.await(), image.await()))
            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> _catsView?.showNoResponseToast()
                    else -> {
                        _catsView?.showToast(ex.message ?: "Error")
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
    }

    fun cancelCatsJob() {
        catsScope.cancel()
    }
}