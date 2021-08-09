package otus.homework.coroutines.presenter

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.utils.CatsService
import otus.homework.coroutines.view.ICatsView
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catFactService: CatsService,
    private val catImageService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope =
        PresenterScope()

    /* MVP */

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val catFactResponse =
                    async(Dispatchers.IO) { catFactService.getCatFact() }
                val catImageResponse =
                    async(Dispatchers.IO) { catImageService.getCatImage() }
//                val catData = CatData(catFactResponse, catImageResponse)

//                _catsView?.populate(catData)

            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервером")
                } else {
                    CrashMonitor.trackWarning()
                    _catsView?.showToast(e.stackTraceToString())
                    e.printStackTrace()
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
            Dispatchers.Main
    }
}