package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsPicService: CatsPicService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()
    private lateinit var job: Job

    fun onInitComplete() {
        job = scope.launch {
            try {
                val fact = getCatFact()
                val picUrl = getCatPictureUrl()
                _catsView?.populate(fact, picUrl)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> _catsView?.showToast("Не удалось получить ответ от сервером")
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showToast(e.message ?: "")
                    }
                }
            }
        }
    }

    private suspend fun getCatFact() = catsService.getCatFact()
    private suspend fun getCatPictureUrl() = catsPicService.getCatPictureUrl().file

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}