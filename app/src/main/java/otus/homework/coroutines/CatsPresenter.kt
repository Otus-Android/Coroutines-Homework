package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsServiceFact: CatsService,
    private val catsServiceImage: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope =
        PresenterScope(Job(), Dispatchers.Main, CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factResponse = getCatFactResponse()
                val imageResponse = getCatImageResponse()
                if (factResponse != null
                    && imageResponse != null ) {
                    val factImage = FactImage(factResponse, imageResponse)
                    _catsView?.populate(factImage)
                } else
                    CrashMonitor.trackWarning()

            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервером")
            } catch (e: Exception) {
                _catsView?.showToast(e.message.toString())
                e.printStackTrace()
            }

        }
    }

    private suspend fun getCatFactResponse(): Fact {
        return catsServiceFact.getCatFact()
    }


    private suspend fun getCatImageResponse(): Image {
        return catsServiceImage.getCatImage()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}

class PresenterScope(
    private val job: Job,
    private val dispatchers: CoroutineDispatcher,
    private val coroutineName: CoroutineName
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = job + dispatchers + coroutineName


}