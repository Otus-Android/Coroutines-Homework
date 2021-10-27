package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val fetchFactAndImageUseCase: FetchFactAndImageUseCase
) : ViewModel() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler(::handleError)) {
            val (fact, randomImage) = fetchFactAndImageUseCase.invoke()
            _catsView?.populate(fact, randomImage)
        }
    }

    private fun handleError(coroutineContext: CoroutineContext, throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException -> _catsView?.showTimeoutError()
            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showError(throwable.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}