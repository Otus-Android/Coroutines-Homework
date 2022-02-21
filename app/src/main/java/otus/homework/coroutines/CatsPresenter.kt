package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.RuntimeException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    var currentRequest: Job? = null

    fun onInitComplete() {
        currentRequest = PresenterScope().launch {
            val response = try {
                withContext(Dispatchers.Default) { catsService.getCatFact() }
            } catch (ex: java.net.SocketTimeoutException) {
                CrashMonitor.trackWarning("Не удалось получить ответ от сервера")
               return@launch
            }
            catch (ex: Exception) {
                CrashMonitor.trackWarning(ex.message?: "Exception")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                _catsView?.populate(response.body()!!)
            } else {
                CrashMonitor.trackWarning(response.message())
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
        currentRequest?.cancel()
    }
}

