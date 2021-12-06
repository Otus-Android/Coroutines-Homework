package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsServiceImg: CatsServiceImg
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {

        presenterScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.logThrowable(throwable)
            throwable.message?.let {
                _catsView?.showToast(it)
            }
        }) {
            supervisorScope {
                try {
                    val response = async {
                        catsService.getCatFact()
                    }

                    val responseImg = async {
                        catsServiceImg.getCatImage()
                    }

                    val resFact = response.await()
                    val resImage = responseImg.await()

                    _catsView?.populate(
                        CatsData(
                            resFact.text,
                            resImage.file
                        )
                    )
                } catch (e: SocketTimeoutException) {
                    _catsView?.networkError()
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