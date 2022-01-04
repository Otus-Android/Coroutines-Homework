package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {

        presenterScope.launch(CoroutineExceptionHandler { _, throwable ->
            presenterScope.launch {
                withContext(Dispatchers.Main) {
                    _catsView?.showToast(throwable.message.orEmpty())
                }
            }
            CrashMonitor.trackWarning()
        }) {
            try {
                val cats = async {
                    catsService.getCatFact()
                }

                val picture = async {
                    catsService.getCatPicture()
                }

                _catsView?.populate(CatModel(cats.await(), picture.await()))

            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _catsView?.showToast("Не удалось получить ответ от сервера")
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

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            Dispatchers.IO + CoroutineName("CatsCoroutine") + SupervisorJob()
    }
}