package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
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
                loadData()
            }
                .onFailure {
                    CrashMonitor.trackWarning()
                    when (it) {
                        is SocketTimeoutException -> {
                            _catsView?.showToast(null)
                        }
                        is CancellationException -> {
                            CrashMonitor.trackWarning()
                            _catsView?.showToast(it.message)
                        }
                        else -> {
                            CrashMonitor.trackWarning()
                            _catsView?.showToast(it.message)
                        }
                    }
                }
                .onSuccess {
                    _catsView?.populate(it)
                }

        }
    }

    private suspend fun loadData() =
        CatsViewState(
            fact = catsService.getCatFact(),
            photo = catsPhotoService.getCatPhoto()
        )

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}