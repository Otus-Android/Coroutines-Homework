package otus.homework.coroutines.presentation.mvp

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.presentation.CatsDataUI
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.ErrorDisplay
import otus.homework.coroutines.utils.ManagerResources
import otus.homework.coroutines.R
import otus.homework.coroutines.data.fact.CatsFactService
import otus.homework.coroutines.data.img.CatsImgService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImgService: CatsImgService,
    private val presenterScope: CoroutineScope,
    private val errorDisplay: ErrorDisplay,
    private val managerResources: ManagerResources
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            CrashMonitor.trackWarning(exception)
        }

        presenterScope.launch(exceptionHandler) {
            try {
                val fact = async { catsFactService.getCatFact() }
                val img = async { catsImgService.getCatImg() }
                _catsView?.populate(CatsDataUI(img.await().imageUrl, fact.await().text))
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    errorDisplay.showMessage(managerResources.getString(R.string.server_error))
                } else {
                    exception.message?.let { errorDisplay.showMessage(it) }
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

    fun stopCoroutines() {
        presenterScope.cancel()
    }
}