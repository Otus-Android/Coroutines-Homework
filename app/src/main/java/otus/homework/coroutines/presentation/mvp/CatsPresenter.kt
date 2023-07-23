package otus.homework.coroutines.presentation.mvp

import kotlinx.coroutines.*
import otus.homework.coroutines.domain.GetCatModelUseCase
import otus.homework.coroutines.presentation.CrashMonitor
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catUseCase: GetCatModelUseCase
) {
    private var _catsView: ICatsView? = null
    private var scope: CoroutineScope? = null

    init {
        scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    }

    fun onInitComplete() {
        scope?.launch {
            try {
                val cat = catUseCase.invoke()
                _catsView?.populate(cat)
            } catch (e: CancellationException) {
                throw e
            } catch (_: SocketTimeoutException) {
                _catsView?.showError()
            } catch (t: Throwable) {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope?.cancel()
    }
}
