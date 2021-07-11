package otus.homework.coroutines

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        catsService.getCatFact().enqueue(object : Callback<List<Fact>> {

            override fun onResponse(call: Call<List<Fact>>, response: Response<List<Fact>>) {
                if (response.isSuccessful && response.body() != null) {
                    val listOfFacts = response.body()!!
                    _catsView?.populate(listOfFacts[0])
                }
            }

            override fun onFailure(call: Call<List<Fact>>, t: Throwable) {
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