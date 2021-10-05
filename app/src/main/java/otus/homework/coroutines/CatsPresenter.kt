package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.model.Cats
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.model.ImageCat
import otus.homework.coroutines.service.CatsService
import otus.homework.coroutines.service.ImageCatsService
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageCatsService: ImageCatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))


    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageCat = async(Dispatchers.IO) { imageCatsService.getImageCat() }
                _catsView?.populate(Cats(fact.await().text, imageCat.await().file ?: ""))
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    _catsView?.showToastTimeout(e)
                } else {
                    CrashMonitor.trackWarning(e)
                    _catsView?.showToastSomeException(e)
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