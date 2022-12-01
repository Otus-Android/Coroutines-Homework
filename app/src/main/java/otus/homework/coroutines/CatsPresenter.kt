package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
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
                val catFact = catsService.getCatFact()
                _catsView?.populate(catFact)
            } catch (e: SocketTimeoutException) {
                _catsView?.showErrorMessage("Не удалось получить ответ от сервера")
            } catch (e: CancellationException) {
                e.message?.let { CrashMonitor.trackWarning(it) }
            }
            catch (e: Exception) {
                e.message?.let {
                    _catsView?.showErrorMessage(it)
                    CrashMonitor.trackWarning(it)
                }
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