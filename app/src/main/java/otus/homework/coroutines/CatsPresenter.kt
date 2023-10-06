package otus.homework.coroutines

import android.util.Log
import androidx.annotation.UiThread
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        /*catsService.getCatFact().enqueue(object : Callback<Fact> {

            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                if (response.isSuccessful && response.body() != null) {
                    _catsView?.populate(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
                CrashMonitor.trackWarning()
            }
        })*/

       presenterScope.launch {
           val fact = catsService.getCatFact()
            /*val imageReq = Picasso.get()
                .load("https://mustafapala.blog/wp-content/uploads/2022/10/avignolu-kizlar.jpg")
            Log.d("CatPresenter", "image request: $imageReq")*/

           _catsView?.populate(fact)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}