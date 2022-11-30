package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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

        presenterScope.launch {
            try {
                val fact = async { catsFactService.getCatFact() }
                val img = async { catsImgService.getCatImg() }
                _catsView?.populate(CatsDataUI(img.await().imageUrl, fact.await().text))
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    errorDisplay.showMessage(managerResources.getString(R.string.server_error))
                } else {
                    CrashMonitor.trackWarning(exception)
                    exception.message?.let { errorDisplay.showMessage(it) }
                    throw exception
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