package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val stringProvider: StringProvider,
    private val scope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    private var job: Job? = null

    fun onInitComplete() {
        if (job?.isActive == true) return

        job = scope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
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