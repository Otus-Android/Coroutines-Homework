package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.util.logging.Logger


class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main) + CoroutineName("CatsCoroutine")

    private var _errorResult: MutableLiveData<Result<CatResult>> = MutableLiveData()

    val errorResult: LiveData<Result<CatResult>>
        get() = _errorResult

    private val errorHandlerException: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Logger.getLogger("CatsPresenter")
                .info("CoroutineExceptionHandler got ${exception.message}")
        }

    fun onInitComplete() {
        coroutineScope.launch(errorHandlerException) {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsService.getCatImage() }

                val fact = factDeferred.await()
                val image = imageDeferred.await()

                if (!fact.isSuccessful && fact.body() == null) {
                    throw IllegalStateException("Incorrect fact response: ${fact.message()}")
                }

                if (!image.isSuccessful && image.body() == null) {
                    throw IllegalStateException("Incorrect image response: ${image.message()}")
                }

                _catsView?.populate(CatResult(fact.body()!!, image.body()!!))
            } catch (exception: Exception) {
                if (exception is SocketTimeoutException) {
                    _errorResult.value = Result.Error
                } else {
                    CrashMonitor.trackWarning()
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

    fun cancel() {
        coroutineScope.launch {
            cancel()
        }
    }
}
