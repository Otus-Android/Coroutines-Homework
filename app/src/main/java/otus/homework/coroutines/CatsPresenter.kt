package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.retrofit.CatsService
import otus.homework.coroutines.retrofit.PicturesService
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CatsPresenter(
    private val catsService: CatsService,
    private val picturesService: PicturesService
) {

    private val presenterJob: Job = SupervisorJob()
    private val presenterScope: CoroutineScope = CoroutineScope((Dispatchers.Main + presenterJob + CoroutineName("CatsCoroutine")))

    private var _catsView: ICatsView? = null

    fun onInitComplete() {

        presenterScope.launch(Dispatchers.IO + handler) {
            try {
                val fact = async{ catsService.getCatFact() }
                val picture =  async { picturesService.getRandomPicture() }
                launch(Dispatchers.Main) {
                    _catsView?.populate(CatsViewState(fact.await(), picture.await()))
                }
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                CrashMonitor.trackWarning()
                launch(Dispatchers.Main) {
                    _catsView?.toast(DisplayError.Timeout)
                }
            }catch (e: UnknownHostException) {
                e.printStackTrace()
                CrashMonitor.trackWarning()
                launch(Dispatchers.Main) {
                    _catsView?.toast(DisplayError.Other(e.message ?: ""))
                }
            }
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterJob.cancel()
        _catsView = null
    }

    private val handler = CoroutineExceptionHandler { _, exception ->

        println("CoroutineExceptionHandler got $exception")
        CrashMonitor.trackWarning()

        val error = when(exception){
            is SocketTimeoutException -> {DisplayError.Timeout}
            else -> {DisplayError.Other(exception.message ?: "Unknown Error")}
        }

        presenterScope.launch {
            _catsView?.toast(error)
        }


    }

}