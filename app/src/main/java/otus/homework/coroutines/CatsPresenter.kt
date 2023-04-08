package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factAsync = async { catsService.getCatFact() }
                _catsView?.populate(factAsync.await().fact)
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast(R.string.error_message)
            }  catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(e.message.toString())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}