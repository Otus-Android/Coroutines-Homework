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
                val facts = withContext(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                withContext(Dispatchers.Main) {
                    try {
                        _catsView?.populate(facts)
                    } catch (ex: Exception) {
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