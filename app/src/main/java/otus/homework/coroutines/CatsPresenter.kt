package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsImageService: CatsImageService,
    private val catFactService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
    )

    fun onInitComplete() {
        coroutineScope.launch {
            runCatching {
                coroutineScope {
                    val fact = async { catFactService.getCatFact() }
                    val imgUrl = async { catsImageService.getCatImage() }
                    CatInfo(fact.await().text, imgUrl.await().url)
                }
            }.onSuccess {
                _catsView?.populate(it)
            }.onFailure {
                when (it) {
                    is CancellationException -> throw it
                    is SocketTimeoutException -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showToast(RealString.Res(R.string.socket_timeout_exception_toast_message))
                    }
                    else -> _catsView?.showToast(RealString.Str(it.message.orEmpty()))
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onActivityStopped() {
        coroutineScope.coroutineContext.cancelChildren()
    }
}

data class CatInfo(
    val fact: String,
    val imgUrl: String
)