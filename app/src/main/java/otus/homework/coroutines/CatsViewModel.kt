package otus.homework.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsViewModel(private val catsService: CatsService) : ViewModel() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, _ ->
            CrashMonitor.trackWarning()
        }) {
            try {
                val factResponseDeferred = async { catsService.getCatFact() }
                val imageResponseDeferred = async { catsService.getCatImage() }
                val factResponse = factResponseDeferred.await()
                val imageResponse = imageResponseDeferred.await()

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
    }

    private fun <T> isResponseSuccessful(response: Response<T>): Boolean {
        return response.isSuccessful && response.body() != null
    }
}