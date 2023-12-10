package otus.homework.coroutines

import android.graphics.Bitmap
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.models.CatFact
import otus.homework.coroutines.models.CatFactPic
import otus.homework.coroutines.models.CatPic
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.URL

class CatsPresenter(
    private val catsService: CatsFactService
) {
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var _catsView: ICatsView? = null
    private val socketTimeoutExceptionMessage = "Не удалось получить ответ от серверов"
    suspend fun onInitComplete() {
        presenterScope.launch {
            var result: Response<CatFact>
            var catFact = ""
            var catPicBitmap: Bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)
            launch {
                try {
                    result = catsService.getCatFact()
                    if (result.isSuccessful && result.body() != null) {
                        catFact = result.body()!!.fact
                    } else {
                        CrashMonitor.trackWarning(null)
                    }
                } catch (socketTimeoutException: SocketTimeoutException) {
                    _catsView?.showToast(socketTimeoutExceptionMessage)
                } catch (e:Exception) {
                    CrashMonitor.trackWarning(e)
                    _catsView?.showToast("Exception message $e")
                }
            }.join()

            launch {
                try {
                    val jsonString =
                        URL("https://api.thecatapi.com/v1/images/search").readText()
                    val catPicUrlList: List<CatPic> = listOf(
                        GsonBuilder().create().fromJson(
                            jsonString,
                            CatPic::class.java
                        )
                    )
                    catPicBitmap = Picasso.get().load(catPicUrlList[0].url).get()
                } catch (socketTimeoutException: SocketTimeoutException) {
                    _catsView?.showToast(socketTimeoutExceptionMessage)
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    _catsView?.showToast("Exception message $e")
                }
            }.join()

            launch {
                _catsView?.populate(CatFactPic(catFact, catPicBitmap))
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