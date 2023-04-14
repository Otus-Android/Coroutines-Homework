package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsFactService,
    private val imageService: CatsImageService
) {

    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch {
            try {
                val fact = catsService.getCatFact()
                val image = imageService.getCatImage().first()
                _catsView?.populate(CatModel(fact, image))
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                e.message?.let { _catsView?.showToast(it) }
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
