package otus.homework.coroutines

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CatsViewModel(
    app: Application,
    private val catsService: CatsService,
    private val resourcesProvider: Resources
) : AndroidViewModel(app) {

    private var _catsView: ICatsView? = null
    private val viewModelScopeWithExceptionHandler = CoroutineScope(
        viewModelScope.coroutineContext +
                CoroutineName(COROUTINE_SCOPE_NAME) +
                CoroutineExceptionHandler
                { _, throwable ->
                    CrashMonitor.trackWarning(throwable.message)
                    _catsView?.inCaseOfError(throwable.message)
                }
    )

    fun onInitComplete() {
        viewModelScopeWithExceptionHandler.launch {
            _catsView?.populate(
                try {
                    Result.Success(
                        CatsData(
                            textFact = catsService.getCatFact().text,
                            imageUrl = catsService.getCatsImageUrl().url
                        )
                    )
                } catch (ex: Exception) {
                    Result.Error(
                        msg = when (ex) {
                            is SocketTimeoutException -> resourcesProvider.getString(R.string.exception_timeout_server_unreached)
                            //is UnknownHostException -> ex.message
                            else -> throw ex
                        },
                        cause = ex
                    )
                }
            )
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        viewModelScope.cancel()
    }

    companion object {
        private const val COROUTINE_SCOPE_NAME = "CatsCoroutine"
    }

}

class CatsViewModelFactory(
    private val app: Application,
    private val catsService: CatsService,
    private val resourcesProvider: Resources
): ViewModelProvider.AndroidViewModelFactory(app) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
            return CatsViewModel(app, catsService, resourcesProvider) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }
}