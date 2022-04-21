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
                    val fact = async(Dispatchers.IO) {
                        catsService.getCatFact()
                    }

                    val image = async(Dispatchers.IO) {
                        catsService.getCatImage()
                    }

                   val result  = awaitAll(fact, image)

                    if (result.size == 2) {
                        val factAndImage = FactAndImage(result[0] as Fact?, result[1] as Image?)
                        _catsView?.populate(factAndImage)
                    } else if (result[0] is Throwable) {
                        throw result[0] as Throwable
                    }

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