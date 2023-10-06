package otus.homework.coroutines

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        catsService.getCatFact().enqueue(object : Callback<Fact> {

            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.e("TAG", "SUCCESS   ${response.body()}")
                    _catsView?.populate(response.body()!!)
                }
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
                Log.e("TAG", "FAILURE")
                CrashMonitor.trackWarning()
            }
        })
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}