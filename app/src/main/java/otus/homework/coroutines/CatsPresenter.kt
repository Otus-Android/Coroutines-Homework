package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import otus.homework.coroutines.network.CatDataRepository
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catDataRepository: CatDataRepository
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            supervisorScope {
                try {
                    val catData = catDataRepository.request()
                    _catsView?.populate(catData)
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