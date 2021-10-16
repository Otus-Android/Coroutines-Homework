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

        presenterScope.launch(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is SocketTimeoutException ->
                    presenterScope.launch {
                        withContext(Dispatchers.Main) {
                            _catsView?.showToast("Не удалось получить ответ от сервера")
                        }
                    }
                else -> presenterScope.launch(Dispatchers.Default) {
                    _catsView?.showToast(throwable.message.orEmpty())
                    CrashMonitor.trackWarning()
                }
            }
        }) {
            val cats = catsService.getCatFact()
            val picture = catsService.getCatPicture()
            withContext(Dispatchers.Main) {
                _catsView?.populate(CatModel(cats, picture))
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
            Dispatchers.IO + CoroutineName("CatsCoroutine")
    }
}