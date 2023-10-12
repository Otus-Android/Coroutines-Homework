package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main +
                CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.populate(
                    catsService.getCatFact()
                )
            } catch (_: SocketTimeoutException) {
                _catsView?.showErrorToast()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showErrorToast(e.message)
            }
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.coroutineContext.cancelChildren()
        _catsView = null
    }

}