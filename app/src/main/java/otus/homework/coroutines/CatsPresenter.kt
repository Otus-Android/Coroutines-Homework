package otus.homework.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.CatRepository
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.StringProvider
import otus.homework.coroutines.utils.coroutines.Dispatcher
import otus.homework.coroutines.utils.coroutines.PresenterScope
import java.net.SocketTimeoutException

class CatsPresenter(
    private val repository: CatRepository,
    private val stringProvider: StringProvider,
    dispatcher: Dispatcher
) {

    private var _catsView: ICatsView? = null

    private val scope = PresenterScope(dispatcher)

    private var job: Job? = null

    fun onInitComplete() {
        if (job?.isActive == true) {
            _catsView?.warn(message = stringProvider.getString(R.string.active_request_warning))
            return
        }

        job = scope.launch {
            try {
                val catInfo = repository.getCatInfo()
                _catsView?.populate(catInfo)
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsView?.warn(message = stringProvider.getString(R.string.timeout_server_error))
                } else {
                    CrashMonitor.trackWarning(e)
                    _catsView?.warn(message = e.messageOrDefault())
                }
            }
        }
    }

    private fun Exception.messageOrDefault() =
        this.message ?: stringProvider.getString(R.string.default_request_error)

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
        job = null
    }
}