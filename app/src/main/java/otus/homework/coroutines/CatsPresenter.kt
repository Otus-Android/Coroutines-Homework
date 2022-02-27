package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsServiceFact: CatsService,
    private val catsServiceImg: CatsService
) {

    private var _catsView: ICatsView? = null
    private var presenterScope: CoroutineScope = PresenterScope()

    fun onInitComplete() {
        presenterScope.launch {
            try {
                coroutineScope {
                    var fact: Fact? = null
                    var image: Image? = null
                    val factRequest = async {
                        fact = withContext(Dispatchers.Default) {
                            catsServiceFact.getCatFact()
                        }
                    }

                    val imageRequest = async {
                        image = withContext(Dispatchers.Default) {
                            catsServiceImg.getCatImage()
                        }
                    }

                    awaitAll(factRequest, imageRequest)
                    _catsView?.populate(IllustratedFact(image!!, fact!!))
                }
            }
            catch (ex: Exception) {
                when (ex) {
                    is SocketTimeoutException -> {
                        _catsView?.showResourceString(R.string.socket_timeout_error)
                    }
                    is CancellationException -> {
                        throw(ex)
                    }
                    else -> {
                        CrashMonitor.trackWarning()
                        _catsView?.showErrorText(ex.message)
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