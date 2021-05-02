package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CatsViewModel(
    private val catsService: CatsService,
    private val imgService: ImgService
) : ViewModel() {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {
        job = viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            CrashMonitor.trackWarning(throwable)
            throwable.message?.let { processResult(Error(it)) }
        }) {
            try {
                val img = async { getImg() }
                val txt = async { getTxt() }
                processResult(Success(Fact(txt.await(), img.await())))
            } catch (e: SocketTimeoutException) {
                processResult(Error("Не удалось получить ответ от сервера"))
            } catch (e: UnknownHostException) {
                processResult(Error("Проверьте интернет-подключение"))
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

    fun processResult(result: Result) {
        when (result) {
            is Success -> _catsView?.populate(result.fact)
            is Error -> _catsView?.toast(result.errorTxt)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        _catsView = null
    }
}

sealed class Result
data class Error(var errorTxt: String) : Result()
data class Success(var fact: Fact) : Result()