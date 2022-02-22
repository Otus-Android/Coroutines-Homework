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
            supervisorScope {
                val fact = async { getFact() }
                val image = async { getRandomImage() }
                _catsView?.populate(FactModel(fact.await()?.text, image.await()?.file))
            }
        }
    }
//Как сделать что бы при ошибке в одном вызове, второй все же выполнился и отрендерился

//    private suspend fun getFact(): Fact? {
//        val responseSuperScope =  supervisorScope {
//            val response = withContext(Dispatchers.Default + handler) {
//                throw Exception()
//                catsService?.getCatFact()
//            }.getBodyOrNull()
//            return@supervisorScope response?.getSuccessData()
//        }
//        return responseSuperScope
//    }

//    У меня получился вариант через try

    //    private suspend fun getFact(): Fact? {
//        val response = try {
//            withContext(Dispatchers.Default + handler) {
//                catsService?.getCatFact()
//            }.getBodyOrNull()
//        } catch (ex: Exception) {
//            return null
//        }
//        return response?.getSuccessData()
//    }
//
    private suspend fun getFact(): Fact? {
         val response = withContext(Dispatchers.Default + handler) {
                catsService?.getCatFact()
            }.getBodyOrNull()
        return response?.getSuccessData()
    }

    private suspend fun getRandomImage(): Image? {
        val response =
            withContext(Dispatchers.Default + handler) { catsService?.getRandomImage() }.getBodyOrNull()
        return response?.getSuccessData()
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
fun <T> Response<T>?.getBodyOrNull(): Result<T>? {
    if (this == null) return null

    return if (this.isSuccessful && this.body() != null) {
        Result.Success(this.body()!!)
    } else {
        CrashMonitor.trackWarning(this.message())
        Result.Error(
            NetworkException(
                this.message(),
                this.code()
            )
        )
    }
}

