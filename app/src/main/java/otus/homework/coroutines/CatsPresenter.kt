package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val meowService: MeowService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(CoroutineName("CatsCoroutine") + Job() + Dispatchers.Main)

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val imageUrl = async(Dispatchers.IO) {
                    meowService.getMeow().file
                }
                _catsView?.populate(Result.Success(UiState(fact.await(), imageUrl.await())))
            } catch (e: Exception) {
                when(e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> _catsView?.populate(Result.Error("Не удалось получить ответ от сервера"))
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.populate(Result.Error(e.message ?: ""))
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
        presenterScope.cancel()
    }
}