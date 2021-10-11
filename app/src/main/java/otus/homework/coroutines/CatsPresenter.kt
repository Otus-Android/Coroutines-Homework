package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
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
    private val imageService: ImageService,
) {
    private var _catsView: ICatsView? = null
    private val presenterScope: CoroutineScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() = presenterScope.launch {
        runCatching { loadData() }
            .onSuccess { result -> _catsView?.populate(result) }
            .onFailure {
                handleError(it)
                cancel()
            }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

    private suspend fun loadData(): CatsViewState = coroutineScope {
        val fact = async { catsService.getCatFact() }
        val image = async { imageService.getRandomImage().file }
        CatsViewState(image.await(), fact.await())
    }

    private fun handleError(throwable: Throwable) = when (throwable) {
        is CancellationException -> throw throwable
        is SocketTimeoutException -> _catsView?.showToast(FAILED_DOWNLOAD_MESSAGE)
        else -> {
            _catsView?.showToast(throwable.message ?: UNKNOWN_ERROR_MESSAGE)
            CrashMonitor.trackWarning()
        }
    }

    companion object {
        private const val FAILED_DOWNLOAD_MESSAGE = "Не удалось загрузить данные с сервера"
        private const val UNKNOWN_ERROR_MESSAGE = "Неизвестная ошибка"
    }
}