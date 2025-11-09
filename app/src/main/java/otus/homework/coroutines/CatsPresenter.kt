package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsServicePics: CatsServicePics
) {
    private var _catsView: ICatsView? = null
    val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        scope.launch {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val imageDeferred = async { catsServicePics.getCatPicUrl() }

                val fact = factDeferred.await()
                val pic = imageDeferred.await()
                val url = pic.firstOrNull()?.url
                fact.url = url.toString()
                _catsView?.populate(fact)
            }
            catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e
                    is SocketTimeoutException -> {
                        _catsView?.showServerErrorToast()
                    }
                    else -> {
                        _catsView?.showToast(e.message.toString())
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
        job.cancel()
    }
}