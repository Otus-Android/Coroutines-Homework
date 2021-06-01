package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsServiceFact: CatsService,
    private val catsServiceImage: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope =
        PresenterScope(Dispatchers.Main, CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factResponse = withContext(Dispatchers.IO) { catsServiceFact.getCatFact() }
                val imageResponse = withContext(Dispatchers.IO) { catsServiceImage.getCatImage() }
                val factImage = FactImage(factResponse, imageResponse)
                _catsView?.populate(factImage)
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    }
                    else -> {
                        _catsView?.showToast(e.message.toString())
                        CrashMonitor.trackWarning()
                        e.printStackTrace()
                    }
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
}

class PresenterScope(
    private val dispatchers: CoroutineDispatcher,
    private val coroutineName: CoroutineName
) : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = dispatchers + coroutineName
}