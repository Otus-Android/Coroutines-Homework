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


    private val handler = CoroutineExceptionHandler { _, exception ->

        println("CoroutineExceptionHandler got $exception")
        CrashMonitor.trackWarning()

        val error = when(exception){
            is SocketTimeoutException -> {DisplayError.Timeout}
            else -> {DisplayError.Other(exception.message ?: "Unknown Error")}
        }

        _catsView?.toast(error)


    }

    private val presenterJob: Job = SupervisorJob()
    private val presenterScope: CoroutineScope = CoroutineScope((Dispatchers.Main + presenterJob + CoroutineName("CatsCoroutine")) + handler)

    private var _catsView: ICatsView? = null

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val fact = async(Dispatchers.IO){ catsService.getCatFact() }
                val picture =  async(Dispatchers.IO){ picturesService.getRandomPicture() }
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


}