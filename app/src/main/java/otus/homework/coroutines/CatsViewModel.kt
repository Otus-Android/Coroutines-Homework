package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val _state = MutableLiveData<Result<CatsInfo>>()
    val state = _state as LiveData<Result<CatsInfo>>

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when(throwable) {
            is SocketTimeoutException -> {
                _state.postValue(Error("Не удалось получить ответ от сервером"))
            }
            else -> {
                CrashMonitor.trackWarning(throwable)
                _state.postValue(Error(throwable.message))
            }
        }
    }

    fun requestCatsInfo() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val factDeferred = async(Dispatchers.IO) { catsService.getCatFact() }
            val imageDeferred = async(Dispatchers.IO) { getRandomImage() }

            val (fact, _) = factDeferred.await()
            val image = imageDeferred.await().file
            _state.postValue(Success(CatsInfo(fact, image)))
        }
    }

    @Throws(NullPointerException::class)
    private fun getRandomImage(): CatsImage {
        val gson = Gson()
        val client = OkHttpClient()
        val request = Request.Builder().url("https://aws.random.cat/meow").build()
        return client.newCall(request).execute().body()?.let {
            gson.fromJson(it.string(), CatsImage::class.java)
        } ?: throw NullPointerException("Image file is null!")
    }
}