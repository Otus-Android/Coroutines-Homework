package otus.homework.coroutines

import kotlinx.coroutines.*
import java.lang.IllegalArgumentException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private lateinit var job: Job

    fun onInitComplete() {
        job = presenterScope.launch {
            supervisorScope {
                try {
                    val result = async(Dispatchers.IO) {
                        catsService.getCatFact()
                    }

                    _catsView?.populate(result.await())

                } catch (exception: Exception) {
                    when (exception) {
                        is CancellationException -> throw exception
                        is SocketTimeoutException -> _catsView?.showError("Не удалось получить ответ от сервера")
                        else -> {
                            CrashMonitor.trackWarning(exception)
                            _catsView?.showError(exception.message)
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
        if (job.isActive) {
            job.cancel()
        }
        _catsView = null
    }
}