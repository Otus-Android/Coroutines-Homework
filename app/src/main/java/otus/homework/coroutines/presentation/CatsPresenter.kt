package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.network.CatImageService
import otus.homework.coroutines.network.CatsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning()
            //_catsView?.showError(throwable.message)
        }) {

            val catImageDeferred = async() {
                try {
                    catImageService.getCatImage()
                } catch (e: Exception) {
                    _catsView?.showError("Не удалось получить ответ от сервера")
                    null
                }
            }

            val catFactDeferred = async() {
                try {
                    catsService.getCatFact()
                } catch (e: SocketTimeoutException) {
                    _catsView?.showError("Не удалось получить ответ от сервера")
                    null
                }
            }

            val fact = catFactDeferred.await()
            val image = catImageDeferred.await()

            val catModel = CatModel(fact, image)
            _catsView?.populate(catModel)
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