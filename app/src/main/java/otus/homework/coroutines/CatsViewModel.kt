package otus.homework.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception

class CatsViewModel: ViewModel() {
      var catsService: CatsService? = null

    private var _catsView: ICatsView? = null

    private var currentRequest: Job? = null

    fun onInitComplete() {
        currentRequest = viewModelScope.launch {
            val fact = async { getFact() }
            val image = async { getRandomImage() }
            _catsView?.populate(FactModel(fact.await()?.text, image.await()?.file))
        }
    }

    // Правильно ли я организовал обработку ошибок?
    private suspend fun getFact(): Fact? {
        val response = try {
            withContext(Dispatchers.Default) { catsService?.getCatFact() }
        } catch (ex: java.net.SocketTimeoutException) {
            CrashMonitor.trackWarning("Не удалось получить ответ от сервера")
            return null
        } catch (ex: Exception) {
            CrashMonitor.trackWarning(ex.message ?: "Exception")
            return null
        }
        return response.getBodyOrNull()
    }

    private suspend fun getRandomImage(): Image? {
        val response = try {
            withContext(Dispatchers.Default) { catsService?.getRandomImage() }
        } catch (ex: java.net.SocketTimeoutException) {
            CrashMonitor.trackWarning("Не удалось получить ответ от сервера")
            return null
        } catch (ex: Exception) {
            CrashMonitor.trackWarning(ex.message ?: "Exception")
            return null
        }

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

