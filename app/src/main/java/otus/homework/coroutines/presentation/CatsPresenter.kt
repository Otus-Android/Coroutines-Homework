package otus.homework.coroutines.presentation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.CatsIconService
import otus.homework.coroutines.domain.CatsIconService.Companion.DEFAULT_ICON
import otus.homework.coroutines.domain.CatsService
import otus.homework.coroutines.domain.CrashMonitor
import otus.homework.coroutines.models.domain.CatFact
import otus.homework.coroutines.models.domain.CatIcon
import otus.homework.coroutines.models.presentation.CatInfoModel
import otus.homework.coroutines.models.presentation.Text
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val iconService: CatsIconService
) {

    private var _catsView: ICatsView? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, exception: Throwable ->
        CrashMonitor.trackWarning(exception.message.orEmpty())
    }

    private val presenterScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + exceptionHandler + CoroutineName("CatsCoroutine"))

    fun onClick() {
        presenterScope.launch {
            try {
                val fact: Deferred<CatFact> = async(Dispatchers.IO) { catsService.getCatFact() }
                val iconDeferred: Deferred<CatIcon> = async(Dispatchers.IO) { iconService.getIcons().first() }

                val icon = runCatching { iconDeferred.await() }.getOrDefault(DEFAULT_ICON)
                _catsView?.populate(CatInfoModel(fact.await().text, icon.url, icon.width, icon.height))
            } catch (ex: Exception) {
                handleError(ex)
            }
        }
    }

    private fun handleError(ex: Throwable) {
        when (ex) {
            is SocketTimeoutException -> {
                _catsView?.showToast(Text.TextByRes(R.string.toast_exception))
            }
            is CancellationException -> {
                throw ex
            }
            else -> {
                val msg = ex.message.toString()
                _catsView?.showToast(Text.TextByString(msg))
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
}