package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ArithmeticException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val factsService: CatsService,
    private val pictureService: PictureService,
    private val presenterScope: CoroutineScope,
) {

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        presenterScope.coroutineContext.cancelChildren()
        when (exception) {
            is SocketTimeoutException -> _catsView?.showToast(R.string.timeout_message)
            else -> {
                presenterScope.launch(Dispatchers.Main){
                    _catsView?.showToast(exception.message)
                }
                CrashMonitor.trackWarning()
            }
        }
    }

    fun onInitComplete() {
        presenterScope.launch(handler) {
            val factDef = async {
                factsService.getCatFact()
            }
            val pictureDef = async {
                pictureService.getCatPicture()
            }
            launch(Dispatchers.Main) {
                val factWithPicture = FactWithPicture(factDef.await()[0].text, pictureDef.await().pictureUrl)
                _catsView?.populate(factWithPicture)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}