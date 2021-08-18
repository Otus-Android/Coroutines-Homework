package otus.homework.coroutines.feature

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.retrofit.CatsImageService
import otus.homework.coroutines.retrofit.CatsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val dispatcherIO: CoroutineDispatcher
) {
    private val presenterScope: CoroutineScope = MainScope() + CoroutineName("CatsCoroutine")

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact: Fact
                withContext(dispatcherIO){
                    fact = catsService.getCatFact()
                }
                _catsView?.populate(CatsData(fact.text, ""))
            }
            catch (e: Exception){
                when(e) {
                    is SocketTimeoutException -> _catsView?.toastSocketTimeoutException(e)
                    else -> {
                        CrashMonitor.trackWarning(e)
                        _catsView?.toastSomeException(e)
                    }
                }
            }
        }
    }

    fun onRefreshComplete() {
        presenterScope.launch {
            try {
                val fact: Fact
                val catImage: CatImage
                withContext(dispatcherIO){
                    fact = catsService.getCatFact()
                    catImage = catsImageService.getCatImage()
                }
                _catsView?.populate(CatsData(fact.text, catImage.file))
            }
            catch (e: Exception){
                when(e) {
                    is SocketTimeoutException -> _catsView?.toastSocketTimeoutException(e)
                    else -> {
                        CrashMonitor.trackWarning(e)
                        _catsView?.toastSomeException(e)
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