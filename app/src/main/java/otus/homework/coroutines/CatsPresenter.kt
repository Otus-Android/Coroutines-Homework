package otus.homework.coroutines

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import otus.homework.coroutines.models.CatFact
import otus.homework.coroutines.models.CatFactPic
import otus.homework.coroutines.models.CatPic
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.URL

class CatsPresenter(
    private val mainActivity: MainActivity,
    private val catsService: CatsFactService
) {
    private var job: Job = Job()
    private var _catsView: ICatsView? = null

    suspend fun onInitComplete() {
        runBlocking {
            job = launch(CoroutineName("CatsCoroutine")) {
                var result: Response<CatFact>
                lateinit var catFact:String
                lateinit var catPicBitmap: Bitmap
                val j1 = launch {
                    try {
                        result = catsService.getCatFact()
                        if (result.isSuccessful && result.body() != null) {
                            catFact = result.body()!!.fact
                        } else {
                            CrashMonitor.trackWarning(null)
                        }
                    } catch (socketTimeoutException: SocketTimeoutException) {
                        Toast.makeText(
                            mainActivity,
                            "Не удалось получить ответ от серверов",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Log.d("otus.homework.coroutines.CrashMonitor", e.toString())
                        Toast.makeText(mainActivity, "exception.message", Toast.LENGTH_SHORT).show()
                    }
                }
                val j2 = CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val jsonString =
                            URL("https://api.thecatapi.com/v1/images/search").readText()
                        val catPicUrl = Gson().fromJson<ArrayList<CatPic>>(jsonString, object :
                            TypeToken<ArrayList<CatPic>>(){}.type)
                        catPicBitmap = Picasso.get().load(catPicUrl[0].url).get()
                    } catch (socketTimeoutException: SocketTimeoutException) {
                        Toast.makeText(
                            mainActivity,
                            "Не удалось получить ответ от серверов",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                joinAll(j1,j2)
                CoroutineScope(Dispatchers.Main).launch{
                    _catsView?.populate(CatFactPic(catFact, catPicBitmap))
                }
            }
            job.join()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}