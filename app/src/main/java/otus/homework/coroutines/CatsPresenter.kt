package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine") +
                CoroutineExceptionHandler { _, _ ->
                    CrashMonitor.trackWarning()
                }
}

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null
    private var scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                supervisorScope {
                    val factResponseDeferred = async { catsService.getCatFact() }
                    val imageResponseDeferred = async { catsService.getCatImage() }
                    val factResponse = factResponseDeferred.await()
                    val imageResponse = imageResponseDeferred.await()

                    if (isResponseSuccessful(factResponse) && isResponseSuccessful(imageResponse)) {
                        _catsView?.populate(
                            CatsInfo(
                                factResponse.body()!!.text,
                                imageResponse.body()!!.file
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                when (ex) {
                    is CancellationException -> throw ex
                    is SocketTimeoutException -> _catsView?.showResult(Error("Не удалось получить ответ от сервера"))
                    else -> ex.message?.let { _catsView?.showResult(Error(it)) }
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

    private fun <T> isResponseSuccessful(response: Response<T>): Boolean {
        return response.isSuccessful && response.body() != null
    }
}