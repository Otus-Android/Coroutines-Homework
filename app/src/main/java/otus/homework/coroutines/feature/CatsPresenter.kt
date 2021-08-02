package otus.homework.coroutines.feature

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.model.CatImage
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Fact
import otus.homework.coroutines.retrofit.CatsImageService
import otus.homework.coroutines.retrofit.CatsService

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService,
    private val dispatcherIO: CoroutineDispatcher
) {
    private val presenterScope: CoroutineScope = MainScope() + CoroutineName("CatsCoroutine")

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            onMakeSomeAction(
                catsService::getCatFact,
                catsImageService::getCatImage,
                false
            )
        }
    }

    fun onRefreshComplete() {
        presenterScope.launch {
            onMakeSomeAction(
                catsService::getCatFact,
                catsImageService::getCatImage,
                true
            )
        }
    }

    private suspend fun onMakeSomeAction(
        getCatFact: suspend () -> Fact?,
        getCatImage: suspend () -> CatImage?,
        flag: Boolean
    ){
        try {
            val fact: Fact?
            val catImage: CatImage?
            if (flag) {
                withContext(dispatcherIO){
                    fact = getCatFact()
                    catImage = getCatImage()
                }
                _catsView?.populate(CatsData(fact?.text ?: NOTHING, catImage?.file ?: ""))
            } else {
                withContext(dispatcherIO){
                    fact = catsService.getCatFact()
                }
                _catsView?.populate(CatsData(fact?.text ?: NOTHING, ""))
            }
        }
        catch (e: java.net.SocketTimeoutException){
            _catsView?.toastSocketTimeoutException(e)
        }
        catch (e: Exception){
            CrashMonitor.trackWarning(e)
            _catsView?.toastSomeException(e)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    companion object {
        const val NOTHING = "empty text"
    }
}