package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import otus.homework.coroutines.models.CatsImage
import otus.homework.coroutines.models.CatsInfo
import java.net.SocketTimeoutException


class CatsViewModel constructor(
    private val catsService: CatsService
) : ViewModel() {

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        viewModelScope.launch {
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
    }
}