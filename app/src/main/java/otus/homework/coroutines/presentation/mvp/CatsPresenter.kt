package otus.homework.coroutines.presentation.mvp

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.domain.CatsRepository
import otus.homework.coroutines.utils.CoroutineDispatchers
import otus.homework.coroutines.utils.CrashMonitor

class CatsPresenter(
    private val catsRepository: CatsRepository,
    coroutineDispatchers: CoroutineDispatchers
) {
    private var _catsView: ICatsView? = null
    private var presenterScope =
        CoroutineScope(coroutineDispatchers.mainDispatcher + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                _catsView?.showProgress()
                _catsView?.populate(catsRepository.getCatRandomFact())
            } catch (e: Exception) {
                val message = e.localizedMessage.orEmpty()
                _catsView?.displayError(message)
                CrashMonitor.trackWarning(message)
            } finally {
                _catsView?.hideProgress()
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