package otus.homework.coroutines

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.view_models.PresenterViewModel



//class CatsPresenter(
//    private val context:Context
//) {
//    private var _catsView: ICatsView? = null
//
//
//
//    suspend fun onInitComplete(){
//        catsService.getCatFact().enqueue(object : Callback<Fact> {
//
//            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
//                if (response.isSuccessful && response.body() != null) {
//                    _catsView?.populate(response.body()!!)
//                }
//            }
//
//            override fun onFailure(call: Call<Fact>, t: Throwable) {
//                CrashMonitor.trackWarning()
//            }
//        })
//
//
//    }
//
//    fun attachView(catsView: ICatsView) {
//        _catsView = catsView
//    }
//
//    fun detachView() {
//        _catsView = null
//    }
//}