package otus.homework.coroutines

import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private val presenterScope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            try{
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            }catch (e: java.net.SocketTimeoutException){
                Util.showToast(R.string.no_connect_server)
            }catch (e:Exception){
                CrashMonitor.trackWarning()
                Util.showToast(e.message.toString())
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