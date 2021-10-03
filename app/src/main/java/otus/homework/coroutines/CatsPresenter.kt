package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService, private val imagesService: ImagesService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    private val exceptionHandler = CoroutineExceptionHandler { context, ex ->
        when (ex) {
            is SocketTimeoutException -> {
                _catsView?.message("Не удалось получить ответ от сервера")
            }
            else -> {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun onInitComplete() {
        presenterScope.launch(exceptionHandler) {
            val fact = presenterScope.async {
                catsService.getCatFact()
            }
            val image = presenterScope.async {
                imagesService.getCatImg()
            }

            _catsView?.populate(Fact(fact.await()), Img(image.await()))
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