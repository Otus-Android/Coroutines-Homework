package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsFactService
import otus.homework.coroutines.api.CatsPhotoService
import otus.homework.coroutines.models.Cat
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsPhotoService: CatsPhotoService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDeferred = async { catsFactService.getCatFact() }
                val photoDeferred = async { catsPhotoService.getCatPhoto() }
                _catsView?.populate(Cat(factDeferred.await(), photoDeferred.await()))
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                _catsView?.showServerResponseError()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                e.message?.let {
                    _catsView?.showError(it)
                }
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