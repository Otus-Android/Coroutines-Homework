package otus.homework.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val catImageService: CatImageService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val catImage = async { catImageService.getCatImage().first().url }
                val catFact = async { catsService.getCatFact().fact }
                _catsView?.populate(
                    CatData(
                        imageUrl = catImage.await(),
                        fact = catFact.await()
                    )
                )
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showToast(e.message.toString())
                CrashMonitor.trackWarning()
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