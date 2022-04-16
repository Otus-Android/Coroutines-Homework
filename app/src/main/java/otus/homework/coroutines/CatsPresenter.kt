package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            supervisorScope {
                try {
                    val catFactResponse = catsService.getCatFact()
                    _catsView?.populate(catFactResponse)
                } catch (e: SocketTimeoutException) {
                    _catsView?.showErrorToast(R.string.network_error)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    e.message?.let { CrashMonitor.trackWarning(it) }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}