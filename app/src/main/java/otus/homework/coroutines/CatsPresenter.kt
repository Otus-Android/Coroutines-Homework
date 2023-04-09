package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val scope by lazy { PresenterScope() }
    private var job : Job? = null

    fun onStop() {
        if (job?.isActive == true) {
            job?.cancel()
        }
    }


    fun onInitComplete() {
        job = scope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            _catsView?.populate(response.body()!!)
                        }
                    } else {
                        CrashMonitor.trackWarning()
                    }
                }
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showToast(R.string.socket_timeout_exception_message)
            } catch (e: java.lang.Exception) {
                _catsView?.showToast("${e.message}")
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