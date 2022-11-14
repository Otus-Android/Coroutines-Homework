package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: CatsImageService
) {

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, e ->
        CrashMonitor.trackWarning(e.message)

        _catsView?.showError(e.message)
    }


    private val job = SupervisorJob()
    private val scope =
        CoroutineScope(Dispatchers.Main + job) + CoroutineName("CatsCoroutine")

    fun onInitComplete() {

        scope.launch(handler) {
            val factAsync = async { catsService.getCatFact() }
            val imageAsync = async { imageService.getCatImage() }

            try {
                val factResult = factAsync.await()
                val imageResult = imageAsync.await()

                if (factResult.isSuccessful && factResult.body() != null &&
                    imageResult.isSuccessful && imageResult.body() != null
                ) {
                    _catsView?.populate(CatEntity(factResult.body()!!, imageResult.body()!!))
                }

            } catch (e: SocketTimeoutException) {
                _catsView?.showError(R.string.timeout_error)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job.cancelChildren()

        _catsView = null
    }
}
