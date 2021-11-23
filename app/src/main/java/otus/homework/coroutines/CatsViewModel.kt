package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class CatsViewModel(
    private val fetchFactAndImageResultUseCase: FetchFactAndImageResultUseCase
) : ViewModel() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        viewModelScope.launch(CoroutineExceptionHandler { _, e -> handleError(e) }) {
            when (val result = fetchFactAndImageResultUseCase.invoke()) {
                is Result.Success -> {
                    val (fact, randomImage) = result.data
                    _catsView?.populate(fact, randomImage)
                }
                is Result.Error -> _catsView?.showTimeoutError()
            }
        }
    }

    private fun handleError(error: Throwable) {
        CrashMonitor.trackWarning()
        _catsView?.showError(error.message)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}