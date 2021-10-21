package otus.homework.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CatsViewModel(
    private val catsService: CatsService,
    private val imgService: ImgService
) : ViewModel() {

    private val _resultLiveData = MutableLiveData<Result>()
    val resultLiveData: LiveData<Result>
        get() = _resultLiveData

    private var job: Job? = null

    fun onInitComplete() {
        job = viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            CrashMonitor.trackWarning(throwable)
            throwable.message?.let { _resultLiveData.value = Result.Error(it) }
        }) {
            try {
                val img = viewModelScope.async(Dispatchers.IO) { getImg() }
                val txt = viewModelScope.async(Dispatchers.IO) { getTxt() }
                _resultLiveData.value = Result.Success(Fact(txt.await(), img.await()))
            } catch (e: SocketTimeoutException) {
                _resultLiveData.value = Result.Error("Не удалось получить ответ от сервера")
            } catch (e: UnknownHostException) {
                _resultLiveData.value = Result.Error("Проверьте интернет-подключение")
            }
        }
    }

    suspend fun getImg(): String {
        val resp = imgService.getCatImg()
        if (resp.isSuccessful && resp.body() != null) {
            return resp.body()!!.file
        }
        return ""
    }

    suspend fun getTxt(): String {
        val resp = catsService.getCatFact()
        if (resp.isSuccessful && resp.body() != null) {
            return resp.body()!!.text
        }
        return ""
    }
}