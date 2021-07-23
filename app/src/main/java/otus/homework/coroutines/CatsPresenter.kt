package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import otus.homework.coroutines.model.CatData
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catFactService: CatsService,
    private val catImageService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope =
        PresenterScope(Dispatchers.Main, CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        presenterScope.launch {

            try {

                val catFactResponse = catFactService.getCatFact()
                val catImageResponse = catImageService.getCatImage()
                val catData = CatData(catFactResponse, catImageResponse)

                _catsView?.populate(catData)

            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsView?.showToast("Не удалось получить ответ от сервером")
                } else {
                    CrashMonitor.trackWarning()
                    _catsView?.showToast(e.stackTraceToString())
                    Log.e("!!!", "onInitComplete: ${e.stackTraceToString()}")
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

    class PresenterScope(
        dispatcher: MainCoroutineDispatcher,
        coroutineName: CoroutineName,
    ) : CoroutineScope {

        override val coroutineContext: CoroutineContext =
            dispatcher + coroutineName
    }
}