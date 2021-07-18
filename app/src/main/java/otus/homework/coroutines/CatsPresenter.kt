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
                val response = factsService.getCatFact()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!![0]
                } else {
                    throw Exception(response.errorBody().toString())
                }
            }
            val pictureDef = async {
                val response = pictureService.getCatPicture()
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                } else {
                    throw Exception(response.errorBody().toString())
                }
            }
            launch(Dispatchers.Main) {
                val factWithPicture = FactWithPicture(factDef.await().text, pictureDef.await().pictureUrl)
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