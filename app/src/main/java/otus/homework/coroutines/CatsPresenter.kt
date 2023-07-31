package otus.homework.coroutines

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: PicturesService
) {

    private var _catsView: ICatsView? = null

    private var job: Job? = null

    fun onInitComplete() {

        _catsView?.also {
            val context = (it as CatsView).context as MainActivity

            job = context.lifecycleScope.launch {
                val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

                val fact = presenterScope.async {
                    catsService.getCatFact().fact
                }

                val pict = presenterScope.async {
                    picsService.getPicture().image
                }

                try {
                    it.populate(TextWithPicture(fact.await(), pict.await()))
                }
                catch (e: SocketTimeoutException) {
                    Toast.makeText(context, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT).show()
                }
                catch (e: Exception) {
                    CrashMonitor.trackWarning()
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

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
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }

    suspend fun getData(): TextWithPicture? = coroutineScope {

        val fact = async {
            catsService.getCatFact().fact
        }

        val pict = async {
            picsService.getPicture().image
        }

        try {
            TextWithPicture(fact.await(), pict.await())
        }
        catch (e: SocketTimeoutException) {
            //Toast.makeText(context, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            CrashMonitor.trackWarning()
            //Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
        }

        null
    }
}
