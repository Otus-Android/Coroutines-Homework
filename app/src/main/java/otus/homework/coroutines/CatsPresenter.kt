package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class PresenterScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineName("CatsCoroutine")
}

class CatsPresenter(private val catsService: CatsService) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = PresenterScope().launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            println(throwable)
        }) {
            try {
                val factResponse = catsService.getCatFact()
                val imageResponse = catsService.getCatImage()
                if (isResponseSuccessful(factResponse) && isResponseSuccessful(imageResponse)) {
                    _catsView?.populate(
                        CatsInfo(
                            factResponse.body()!!.text,
                            imageResponse.body()!!.file
                        )
                    )
                }
            } catch (ex: SocketTimeoutException) {
                _catsView?.showError("Не удалось получить ответ от сервера")
            } catch (ex: Exception) {
                if (ex !is CancellationException) {
                    CrashMonitor.trackWarning()
                    ex.message?.let { _catsView?.showError(it) }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }

    private fun <T> isResponseSuccessful(response: Response<T>): Boolean {
        return response.isSuccessful && response.body() != null
    }
}