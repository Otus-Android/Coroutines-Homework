package otus.homework.coroutines

import otus.homework.coroutines.model.Fact
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        catsService.getCatFact().enqueue(object : Callback<Fact> {

            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                if (response.isSuccessful) {
                    response.body()?.let { body -> _catsView?.populate(body) }
                }
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
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