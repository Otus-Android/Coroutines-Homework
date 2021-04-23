package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private var catsJob: Job? = null

    fun onInitComplete() {
        catsJob?.cancel()
        catsJob = PresenterScope().launch {
            try{
                val fact = async { catsService.getCatFact() }
                val image = async { imageService.getRandomImage()}
                _catsView?.populate(CatsModel(fact.await(), image.await()))
            } catch (socketEx: SocketTimeoutException){
                _catsView?.showNoResponseToast()
            } catch (ex: Exception){
                _catsView?.showToast(ex.message ?: "Error")
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelCatsJob(){
        catsJob?.cancel()
    }
}