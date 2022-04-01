package otus.homework.coroutines

import kotlinx.coroutines.*
import retrofit2.Response
import java.lang.Exception

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private var scope: CoroutineScope = PresenterScope()


    fun onInitComplete() {
        scope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getRandomImage() }
                _catsView?.populate(FactModel(fact.await().text, image.await().file))
            } catch (ex: java.net.SocketTimeoutException) {
                CrashMonitor.trackWarning("Не удалось получить ответ от сервера")
            } catch (ex: Exception) {
                CrashMonitor.trackWarning(ex.message ?: "Exception")
            }
        }
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
        scope.cancel()
    }
}

//Уместно ли использовать нечто подобное?д
/**
 * Return body if [isSuccessful] is true
 * or [null]
 **/
fun <T> Response<T>.getBodyOrNull(): T? {
    return if (this.isSuccessful) {
        this.body()
    } else {
        CrashMonitor.trackWarning(this.message())
        null
    }
}

