package otus.homework.coroutines.presentation

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.data.CatsRepo
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsRepo: CatsRepo,
    private val presenterScope: PresenterScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.displayData(catsRepo.getCatsFactsWithPhoto())
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.displayError(CatsError.NetworkError)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.displayError(CatsError.DefaultError(e.message))
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}