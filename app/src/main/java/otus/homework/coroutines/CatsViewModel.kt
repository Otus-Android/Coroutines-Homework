package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = viewModelScope.launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
        }) {
            try {
                val factResponse = catsService.getCatFact()
                val imageResponse = catsService.getCatImage()
                if (isResponseSuccessful(factResponse) && isResponseSuccessful(imageResponse)) {
                    _catsView?.showResult(
                        Success(
                            CatsInfo(
                                factResponse.body()!!.text,
                                imageResponse.body()!!.file
                            )
                        )
                    )
                }
            } catch (ex: SocketTimeoutException) {
                _catsView?.showResult(Error("Не удалось получить ответ от сервера"))
            } catch (ex: Exception) {
                if (ex !is CancellationException) {
                    CrashMonitor.trackWarning()
                    ex.message?.let {
                        _catsView?.showResult(Error(it))
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
        job?.cancel()
    }

    private fun <T> isResponseSuccessful(response: Response<T>): Boolean {
        return response.isSuccessful && response.body() != null
    }
}