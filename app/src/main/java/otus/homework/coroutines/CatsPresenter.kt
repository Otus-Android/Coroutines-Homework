package otus.homework.coroutines
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {
    private var _catsView: ICatsView? = null
    private var job: Job? = null
    fun onInitComplete() {
        job = PresenterScope(
            CoroutineExceptionHandler {
                    _, throwable -> println("CoroutineExceptionHandler ${throwable.message}")
            }).launch {
            coroutineScope {
                    try {
                        val fact = async(Dispatchers.IO) {
                            catsService.getCatFact()
                        }
                        val image = async(Dispatchers.IO) {
                            catsService.getCatImage()
                        }
                        image.start()
                        fact.start()
                        withContext(Dispatchers.Main) {
                            _catsView?.populate(fact.await(), image.await().file)
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            when (ex) {
                                is CancellationException -> throw ex
                                is SocketTimeoutException -> _catsView?.showToastMessage("Не удалось получить ответ от сервером")
                                else -> {
                                    ex.message?.let { _catsView?.showToastMessage(it) }
                                    CrashMonitor.trackWarning(ex)
                                }
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
        job?.cancel()
        _catsView = null
    }

    class PresenterScope(
        override val coroutineContext: CoroutineContext =
            Dispatchers.Main +
                    CoroutineName("CatsCoroutine")
    ) : CoroutineScope
}