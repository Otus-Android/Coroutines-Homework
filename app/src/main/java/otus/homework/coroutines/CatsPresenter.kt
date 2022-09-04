package otus.homework.coroutines

import android.content.res.Resources
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
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
                { _, throwable ->
                    CrashMonitor.trackWarning(throwable.message)
                    throwable.message?.let { _catsView?.showShortToast(it) }
                }
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                coroutineScope {
                    val factDeferred = async { catsService.getCatFact().text }
                    val imageDeferred = async { catsService.getCatsImageUrl().url }
                    _catsView?.populate(
                        Result.Success(
                            CatsData(
                                textFact = factDeferred.await(),
                                imageUrl = imageDeferred.await()
                            )
                        )
                    )
                }
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