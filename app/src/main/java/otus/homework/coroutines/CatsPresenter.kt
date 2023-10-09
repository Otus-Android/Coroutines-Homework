package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.repo.Repository
import java.net.SocketTimeoutException

class CatsPresenter(
    private val repository: Repository
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName(name = "CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val meowInfo = repository.getMeowInfo()
                _catsView?.populate(meowInfo)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                e.message?.let { msg ->_catsView?.showToast(msg) }
                CrashMonitor.trackWarning(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        presenterScope.cancel()
    }
}