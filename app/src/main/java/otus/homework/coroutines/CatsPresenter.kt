package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.FullCatFact
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.PicturesService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picturesService: PicturesService,
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factDeferred = async { catsService.getCatFact() }
                val pictureDeferred = async { picturesService.getPic() }
                val fact = factDeferred.await()
                val picture = pictureDeferred.await()

                _catsView?.populate(FullCatFact(fact, picture.first().url))
            } catch (e: SocketTimeoutException) {
                _catsView?.showErrorToast("Не удалось получить ответ от сервером")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                _catsView?.showErrorToast(e.message ?: "Unknown error")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}