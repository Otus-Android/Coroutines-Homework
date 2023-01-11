package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(
        Job() + Dispatchers.Main + CoroutineName(COROUTINE_NAME)
    )

    fun onInitComplete() {
        presenterScope.launch {
            try {
                doRequest()
            } catch (e: SocketTimeoutException) {
                showMessage(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                showMessage(e.message)
            }
        }
    }

    private suspend fun doRequest() {
        val response = catsService.getCatFact()
        if (response.isSuccessful && response.body() != null) {
            response.body()?.let { fact ->
                _catsView?.populate(fact)
            }
        }
    }

    private fun showMessage(message: String?) {
        _catsView?.showToast(message ?: UNKNOWN_ERROR)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

    private companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
        private const val UNKNOWN_ERROR = "Неизвестная ошибка"
        private const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"
    }
}