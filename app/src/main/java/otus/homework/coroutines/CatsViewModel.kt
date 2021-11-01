package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsViewModel(
    private val fetchFactAndImageResultUseCase: FetchFactAndImageResultUseCase
) : ViewModel() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        viewModelScope.launch {
            when (val result = fetchFactAndImageResultUseCase.invoke()) {
                is Result.Success -> {
                    val (fact, randomImage) = result.data
                    _catsView?.populate(fact, randomImage)
                }
                is Result.Error -> handleError(result.error)
            }
        }
    }

    private fun handleError(error: Exception) {
        when (error) {
            is SocketTimeoutException -> _catsView?.showTimeoutError()
            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showError(error.message)
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