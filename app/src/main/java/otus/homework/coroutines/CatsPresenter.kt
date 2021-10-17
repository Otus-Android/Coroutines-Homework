package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import otus.homework.coroutines.models.PresentModel
import otus.homework.coroutines.services.ImageService
import otus.homework.coroutines.utils.PresenterScope
import java.net.SocketTimeoutException

const val TAG = "Presenter"

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService


) {
    private val presentScope = PresenterScope()

    private var _catsView: ICatsView? = null

    private val exceptionHandler = SupervisorJob() + CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning(exception)
    }

    fun onInitComplete() {
        presentScope.launch(exceptionHandler) {
            try {
                val requestFact = async() { catsService.getCatFact() }
                val requestImage = async() { imageService.getCatImage() }
                val responseFact = requestFact.await()
                val responseImage = requestImage.await()

                if (responseFact.isSuccessful && responseImage.isSuccessful) {
                    val presentModel = PresentModel(responseImage.body()!!, responseFact.body()!!)
                    _catsView?.populate(presentModel)
                }
            } catch (cause: Exception) {
                when (cause) {
                    is SocketTimeoutException -> _catsView?.showToast("Не удалось получить ответ от сервера")
                    is CancellationException -> throw cause
                    else ->  _catsView?.showToast("Exception: $cause")
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presentScope.cancel()
    }

}