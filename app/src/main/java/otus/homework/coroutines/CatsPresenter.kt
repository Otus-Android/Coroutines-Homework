package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsPhotoService: CatsPhotoService
) {

    private var _catsView: ICatsView? = null
    private val scope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob())

    fun onInitComplete() {
        scope.launch {
            runCatching {
                val fact = getCatFact().await()
                val photo = getCatPhoto().await()
                CatsViewState(
                    fact = fact,
                    photo = photo
                )
            }
                .onFailure {
                    when (it) {
                        is SocketTimeoutException -> {
                            _catsView?.showToast(null)
                            CrashMonitor.trackWarning()
                        }
                        is CancellationException -> {
                            CrashMonitor.trackWarning()
                        }
                        else -> {
                            CrashMonitor.trackWarning()
                        }
                    }
                }
                .onSuccess {
                    _catsView?.populate(it)
                }

        }
    }

    private suspend fun getCatFact() = withContext(Dispatchers.IO) {
        async { catsService.getCatFact() }
    }

    private suspend fun getCatPhoto() = withContext(Dispatchers.IO) {
        async { catsPhotoService.getCatPhoto() }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}