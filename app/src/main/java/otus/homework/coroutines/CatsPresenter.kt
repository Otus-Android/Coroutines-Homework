package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val fetchFactAndImageUseCase: FetchFactAndImageUseCase
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = PresenterScope.launch {
            try {
                val (fact, randomImage) = fetchFactAndImageUseCase.invoke()
                _catsView?.populate(fact, randomImage)
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        when (e) {
            is SocketTimeoutException -> _catsView?.showTimeoutError()
            else -> {
                CrashMonitor.trackWarning()
                _catsView?.showError(e)
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
        job = null
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}