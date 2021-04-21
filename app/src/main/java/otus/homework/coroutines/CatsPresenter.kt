package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    var catsJob: Job? = null

    fun onInitComplete() {
        catsJob = PresenterScope().launch {
            try{
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
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
}