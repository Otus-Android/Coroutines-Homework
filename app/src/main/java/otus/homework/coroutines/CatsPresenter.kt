package otus.homework.coroutines

import android.support.annotation.RestrictTo.Scope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {
    inner class MyScope : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main + CoroutineName("CatsCoroutine")

    }

    private var _catsView: ICatsView? = null
    private var job: Job? = null
    fun onInitComplete() {
        job = MyScope().launch {

    val fact = async {
        return@async try {
            catsService.getCatFact()
        }
        catch (e: SocketTimeoutException){
            CrashMonitor.trackWarning()
            _catsView?.showError(e.message.toString())
        } as Fact
    }.await()
    val img = async {
        return@async try {
            catsService.getCatFactImg().first()
        }
        catch (e: SocketTimeoutException){
            CrashMonitor.trackWarning()
            _catsView?.showError(e.message.toString())
        } as Img
        }.await()




            _catsView?.populate(fact,img)
/*                try {
                    _catsView?.populate(catsService.getCatFact(),catsService.getCatFactImg().first())
                } catch (e: SocketTimeoutException) {
                    CrashMonitor.trackWarning()
                    _catsView?.showError(e.message.toString())
                }*/

/*                try {
                  //  _catsView?.populate(catsService.getCatFact())
                    // catsService.getCatFactImg()
                } catch (e: SocketTimeoutException) {

                }*/


        }

        /*        catsService.getCatFact().enqueue(object : Callback<Fact> {

                    override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                        if (response.isSuccessful && response.body() != null) {
                            _catsView?.populate(response.body()!!)
                        }
                    }

                    override fun onFailure(call: Call<Fact>, t: Throwable) {
                        CrashMonitor.trackWarning()
                    }
                })*/
    }

    suspend fun req(){

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {

        _catsView = null

    }

}