package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.Exception

class CatsViewModel : ViewModel() {
    var catsService: CatsService? = null

    private var _catsView: ICatsView? = null

    private var currentRequest: Job? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        if (exception is java.net.SocketTimeoutException)
            CrashMonitor.trackWarning("Не удалось получить ответ от сервера")
        else {
            CrashMonitor.trackWarning("CoroutineExceptionHandler got $exception.message")
        }
    }

    fun onInitComplete() {
        currentRequest = viewModelScope.launch(handler) {
            val fact = async { getFact() }
            val image = async { getRandomImage() }
            _catsView?.populate(FactModel(fact.await()?.text, image.await()?.file))
        }
    }

    private suspend fun getFact(): Fact? {
        val response = withContext(Dispatchers.Default) { catsService?.getCatFact() }
        return response.getBodyOrNull()
    }

    private suspend fun getRandomImage(): Image? {
        val response = withContext(Dispatchers.Default) { catsService?.getRandomImage() }
        return response.getBodyOrNull()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun onViewStop() {
        detachView()
        cancelRequest()
    }

    private fun detachView() {
        _catsView = null
    }

    private fun cancelRequest() {
        currentRequest?.cancel()
    }
}

//Уместно ли использовать нечто подобное?д
/**
 * Return body if [isSuccessful] is true
 * or [null]
 **/
fun <T> Response<T>?.getBodyOrNull(): T? {
    if (this == null) return null

    return if (this.isSuccessful) {
        this.body()
    } else {
        CrashMonitor.trackWarning(this.message())
        null
    }
}

