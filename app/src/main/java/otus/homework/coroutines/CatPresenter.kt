package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatPresenter(
    private val catsService: CatService,
    private val catsImageService: CatService
) {

    private var presenterScope: CoroutineScope = PresenterScope
    private var _catsView: ICatView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val factResponse =
                    withContext(Dispatchers.Default) { catsService.getCatFact() }

                val imageResponse =
                    withContext(Dispatchers.Default) { catsImageService.getCatImage() }

                _catsView?.populate(FactAndImage(factResponse.text, imageResponse.file))

            } catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsView?.showToastMessage(R.string.socket_timeout_message.toString())
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showToastMessage(ex.message ?: "")
                    }
                }
            }
        }
    }

    fun attachView(catsView: ICatView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}