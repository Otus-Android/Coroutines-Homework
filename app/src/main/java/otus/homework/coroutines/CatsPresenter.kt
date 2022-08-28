package otus.homework.coroutines

import android.content.res.Resources
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val resourcesProvider: Resources
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(
        Dispatchers.Main +
                CoroutineName(COROUTINE_SCOPE_NAME) +
                CoroutineExceptionHandler
                { coroutineContext, throwable ->
                    CrashMonitor.trackWarning()
                    throwable.message?.let { _catsView?.showShortToast(it) }
                }
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.populate(
                    CatsData(
                        textFact = catsService.getCatFact().text,
                        imageUrl = catsService.getCatsImageUrl().url
                    )
                )
            } catch (ex: SocketTimeoutException) {
                _catsView?.showShortToast(resourcesProvider.getString(R.string.exception_timeout_server_unreached))
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    companion object {
        private const val COROUTINE_SCOPE_NAME = "CatsCoroutine"
    }
}