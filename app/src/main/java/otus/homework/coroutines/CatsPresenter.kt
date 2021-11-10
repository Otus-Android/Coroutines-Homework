package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private var job: Job? = null

    private val presenterScope = PresenterScope()

    private val exceptionHandler = CoroutineExceptionHandler { _, e ->
        _catsView?.showToast("Ошибка ${e.message}")
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() {
        job = presenterScope.launch(exceptionHandler) {
            try {
                val fact = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val image = async(Dispatchers.IO) {
                    catsService.getCatImage()
                }
                _catsView?.populate(CatModel(fact.await(), image.await()))
            } catch (exception: Exception) {
                when (exception) {
                    is SocketTimeoutException -> {
                        _catsView?.showToast("Не удалось получить ответ от сервера")
                    }
                    is CancellationException -> {
                        throw exception
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
    }

    fun onStop() {
        job?.cancel()
    }
}