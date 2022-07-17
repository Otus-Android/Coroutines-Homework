package otus.homework.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                _catsView?.populate(catsService.getCatFact())
            } catch (e: Exception) {
                when(e) {
                    is SocketTimeoutException -> _catsView?.showErrorMessage("Не удалось получить ответ от сервером")
                    else -> {
                        e.message?.let {
                            _catsView?.showErrorMessage(it)
                            CrashMonitor.trackWarning(it)
                        }
                    }
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