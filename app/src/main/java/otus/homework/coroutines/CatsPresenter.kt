package otus.homework.coroutines

import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import otus.homework.coroutines.models.CatsImage
import otus.homework.coroutines.models.CatsInfo
import java.lang.RuntimeException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val coroutineScope = PresenterScope()

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch {
            try {
                val factDeferred = async(Dispatchers.IO) { catsService.getCatFact() }
                val imageDeferred = async(Dispatchers.IO) {
                    val gson = Gson()
                    val client = OkHttpClient()
                    val request = Request.Builder().url("https://aws.random.cat/meow").build()
                    client.newCall(request).execute().body()?.let {
                        gson.fromJson(it.string(), CatsImage::class.java)
                    } ?: throw NullPointerException("Image file is null!")
                }

                val (fact, _) = factDeferred.await()
                val image = imageDeferred.await().file
                _catsView?.populate(CatsInfo(fact, image))
            } catch (exception: SocketTimeoutException) {
                _catsView?.displayErrorMessage("Не удалось получить ответ от сервером")
            } catch (exception: RuntimeException) {
                CrashMonitor.trackWarning()
                exception.message?.let { message ->
                    _catsView?.displayErrorMessage(message)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        coroutineScope.coroutineContext.cancelChildren()
    }
}

fun PresenterScope() = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))