package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService
) {
    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val catsFact = async { catsFactService.getCatFact() }
                val catsImage = async { catsImageService.getImage().first() }
                _catsView?.populate(CatModal(
                    catsFact = catsFact.await(),
                    catsImage = catsImage.await()
                ))
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast(R.string.error_failed)

            } catch (e: CancellationException) {
                _catsView?.showToast(e.message.orEmpty())
                CrashMonitor.trackWarning(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }
}