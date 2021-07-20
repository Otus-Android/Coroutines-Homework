package otus.homework.coroutines

import android.widget.Toast
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val job = SupervisorJob()
    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        if (e is SocketTimeoutException) {
            _catsView?.showErrorToast("timeout exception")
        } else {
            CrashMonitor.trackWarning(e.message)
            _catsView?.showErrorToast("Error occurred: ${e.message}")
        }
    }
    private val coroutineContext: CoroutineContext =
        Dispatchers.Main + job + CoroutineName("CatsCoroutine") + exceptionHandler
    private val customPresenterScope = CoroutineScope(coroutineContext)

//    fun onInitComplete() {
//        customPresenterScope.launch {
//            getData()
//        }
//    }

    private suspend fun getData() {
        customPresenterScope.launch {
            val fact = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
            }

            val image = withContext(Dispatchers.IO) {
                catsService.getCatImage()
            }

            val data = CustomCatPresentationModel(
                fact[0].fact,
                image.file
            )
            _catsView?.populate(data)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun stopJob() {
        customPresenterScope.cancel()
    }


}