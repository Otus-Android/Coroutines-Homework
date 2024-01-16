package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private var fact : Fact = Fact("no fact",0)

    suspend fun onInitComplete() {

        try {
            withContext(Dispatchers.IO) {
                fact = catsService.getCatFact()
                Log.d("servise Cat", "fact ${fact.fact}")
            }
                _catsView?.populate(fact)

        } catch (e: Exception) {
            CrashMonitor.trackWarning(e)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}