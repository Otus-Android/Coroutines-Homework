package otus.homework.coroutines

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val app: Application,
    private val catsService: CatsService
) : AndroidViewModel(app) {

    private var _catsView: ICatsView? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        CrashMonitor.trackWarning(throwable.message)
    }

    fun onInitComplete() {
        viewModelScope.launch(
            CoroutineName(COROUTINE_SCOPE_NAME) +
            coroutineExceptionHandler
        ) {
            _catsView?.populate(
                concatResults(finalResultCreation = { list ->
                    val fact: Fact? = list.find { it is Fact } as Fact?
                    val image: Image? = list.find { it is Image } as Image?
                    CatsData(
                        fact?.text,
                        image?.url
                    )
                }, listOf(
                    makeRequestAndCatchErrorsAsync( catsService::getCatFact),
                    makeRequestAndCatchErrorsAsync( catsService::getCatsImageUrl)
                ).awaitAll())
            )
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    private fun <T: Any> CoroutineScope.makeRequestAndCatchErrorsAsync(
        request: suspend () -> T
    ): Deferred<Result<T>> {
        return async {
            Log.d(request.javaClass.toString(), "Start ${System.currentTimeMillis()}")
            try {
                Result.Success(
                    request.invoke()
                )
            } catch (ex: Exception) {
                Result.Error(
                    msg = when (ex) {
                        is SocketTimeoutException -> app.resources.getString(R.string.exception_timeout_server_unreached)
                        is CancellationException -> throw ex
                        else -> {
                            _catsView?.inCaseOfError(ex.message)
                            throw ex
                        }
                    },
                    cause = ex
                )
            } finally {
                Log.d(request.javaClass.toString(), "End ${System.currentTimeMillis()}")
            }
        }
    }

    companion object {
        private const val COROUTINE_SCOPE_NAME = "CatsCoroutine"
    }

}

class CatsViewModelFactory(
    private val app: Application,
    private val catsService: CatsService
): ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(app, catsService) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}