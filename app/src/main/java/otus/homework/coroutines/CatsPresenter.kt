package otus.homework.coroutines

import kotlinx.coroutines.*
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsTextService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val catsText = async (Dispatchers.IO) { catsTextService.getCatFact() }
                val catsImage = async (Dispatchers.IO) { catsImageService.getCatImage() }
                _catsView?.populate(CatsData.create(catsText.await(), catsImage.await()))
            } catch (e: SocketTimeoutException) {
                _catsView?.showErrorMessage("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                e.message?.let { _catsView?.showErrorMessage(it) }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() = scope.cancel()
}

class PresenterScope: CoroutineScope {
    override val coroutineContext: CoroutineContext
        get () = SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
}