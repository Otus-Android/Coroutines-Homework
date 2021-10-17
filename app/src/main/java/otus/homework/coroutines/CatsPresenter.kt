package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.Result.Success
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private val scope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onMoreFacts() = with(catsService) {
        scope.launch {
            supervisorScope {
                try {
                    val catFact = async { getCatFact().text }
                    val catPhotoUrl = async { getCatPhoto().url }
                    withContext(Dispatchers.IO) {
                        _catsView?.populate(Success(CatData(catFact.await(), catPhotoUrl.await())))
                    }
                } catch (ex: Exception) {
                    when (ex) {
                        is CancellationException -> throw ex
                        is SocketTimeoutException ->
                            _catsView?.showLoadError("Failed to get a response from the server")
                        else -> {
                            CrashMonitor.trackWarning(ex)
                            _catsView?.showLoadError(ex.message ?: "Unknown message")
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
        _catsView = null
        scope.cancel()
    }

    private class PresenterScope : CoroutineScope {

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
    }
}
