package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.api.CatFactService
import otus.homework.coroutines.api.CatPhotoService
import otus.homework.coroutines.data.CatDTO
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatFactService,
    private val catPhotoService: CatPhotoService
) {
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineName("CatsCoroutine"))

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch(exceptionHandler) {
            val downloadFactJob = async {
                catFactService.getCatFact()
            }

            val downloadPhotoJob = async {
                catPhotoService.getCatPhoto()
            }

            val catDTO = CatDTO(
                photo = downloadPhotoJob.await(),
                fact = downloadFactJob.await()
            )
            _catsView?.populate(catDTO)
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is SocketTimeoutException) {
            _catsView?.showServerResponseError()
        } else {
            throwable.message?.let { errorMessage ->
                _catsView?.showError(errorMessage)
            }
            CrashMonitor.trackWarning()
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