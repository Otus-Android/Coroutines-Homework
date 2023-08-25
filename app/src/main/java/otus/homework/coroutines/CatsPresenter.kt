package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val serviceCatFact: CatsService,
    private val serviceCatImage: CatsService
) {

    private var _catsView: ICatsView? = null
    private val outerScope = PresenterScope()
    private lateinit var innerScope: CoroutineScope

    fun onInitComplete() {
        outerScope.launch {
            innerScope = CoroutineScope(Dispatchers.IO)
            try {
                val catFact = innerScope.async { serviceCatFact.getCatFact() }
                val catImage = innerScope.async { serviceCatImage.getCatImage() }
                _catsView?.populate(CatModel(catFact.await().fact, catImage.await().url))
            } catch (exc: SocketTimeoutException) {
                _catsView?.makeToast("Не удалось получить ответ от сервером")
            } catch (exc: Exception) {
                CrashMonitor.trackWarning(exc)
                _catsView?.makeToast(exc.message.toString())
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        outerScope.cancel()
        innerScope.cancel()
    }
}