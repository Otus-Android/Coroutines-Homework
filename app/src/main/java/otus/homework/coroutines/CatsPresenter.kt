package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable)
        _catsView?.showToast(throwable.message)
    }

    fun onInitComplete() {
        scope.launch(exceptionHandler) {
            try {
                val response = catsService.getCatFact()
                val fact = response.body()
                if (response.isSuccessful && fact != null) {
                    _catsView?.populate(fact)
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.coroutineContext.cancel()
        _catsView = null
    }
}