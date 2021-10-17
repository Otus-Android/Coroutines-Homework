package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private var job: Job? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                val image = catsService.getCatImage()

                _catsView?.populate(CatModel(fact, image))
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")
                } else {
                    _catsView?.showToast("Ошибка ${exception.message}")
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        job?.cancel()
    }
}