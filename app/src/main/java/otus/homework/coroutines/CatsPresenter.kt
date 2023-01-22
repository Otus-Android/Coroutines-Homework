package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val meowService: MeowService
) {
    private var _catsView: ICatsView? = null

    private var job: Job = Job()

    private val presenterScope: CoroutineScope =
        CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main + job)

    fun onInitComplete() {
        presenterScope.launch {
            supervisorScope {
                val fact = async { catsService.getCatFact() }
                val image = async { meowService.getImage() }
                try {
                    _catsView?.populate(Result.Success(UiState(fact.await().text, image.await().url)))
                } catch (e: Exception) {
                    when (e) {
                        is CancellationException -> throw e
                        is SocketTimeoutException -> _catsView?.populate(Result.Error("Не удалось получить ответ от сервером"))
                        else -> {
                            _catsView?.populate(Result.Error(e.message.toString()))
                            CrashMonitor.trackWarning()
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
        presenterScope.cancel()
        _catsView = null
    }
}