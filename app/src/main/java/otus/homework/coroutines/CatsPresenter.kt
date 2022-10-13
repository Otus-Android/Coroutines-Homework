package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope: PresenterScope by lazy { PresenterScope() }
    private var _catsView: ICatsView? = null
    private var catJob: Job? = null

    fun onInitComplete() {
        try {
            presenterScope.launch {
                val cat = withContext(Dispatchers.IO) {
                    return@withContext catsService.getCatFact()
                }
                _catsView?.populate(cat)
            }
        } catch (ste: SocketTimeoutException) {
            _catsView?.showToast("Не удалось получить ответ от сервером")
        } catch (e: Exception) {
            e.message?.let {
                _catsView?.showToast(it)
                CrashMonitor.trackWarning(this.javaClass.name, it)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catJob?.cancel()
        _catsView = null
    }
}