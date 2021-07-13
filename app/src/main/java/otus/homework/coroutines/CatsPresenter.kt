package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
        private val catsService: CatsService
) {
    private val coroutineContext: CoroutineContext = Dispatchers.Main + CoroutineName(CATS_COROUTINE)
    private val presenterScope = CoroutineScope(coroutineContext)

    private val _error = MutableLiveData<ErrorState>()
    val error: LiveData<ErrorState> = _error

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val catsFact = catsService.getCatFact()
                _catsView?.populate(catsFact)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _error.value = ErrorState.SocketError
                    }
                    else -> {
                        _error.value = ErrorState.OtherError(message = e.message)
                        CrashMonitor.trackWarning()
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
        coroutineContext.cancel()
    }

    companion object {
        const val CATS_COROUTINE = "CatsCoroutine"
    }
}