package otus.homework.coroutines

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
                val result = catsService.getCatFact()
                if (!result.isSuccessful && result.body() == null) return@launch

                _catsView?.populate(result.body()!!)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервером")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(e.localizedMessage!!)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.onStop()
        _catsView = null
    }
}
