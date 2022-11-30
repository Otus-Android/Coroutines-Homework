package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: CoroutineScope,
    private val errorDisplay: ErrorDisplay,
    private val managerResources: ManagerResources
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    errorDisplay.showMessage(managerResources.getString(R.string.server_error))
                } else {
                    CrashMonitor.trackWarning(exception)
                    exception.message?.let { errorDisplay.showMessage(it) }
                }
                throw exception
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