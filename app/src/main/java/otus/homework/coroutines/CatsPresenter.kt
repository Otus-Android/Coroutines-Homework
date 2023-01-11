package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

private const val COROUTINE_NAME = "CatsCoroutine"
private const val TIME_OUT_ERROR_TOAST_MSG = "Не удалось получить ответ от сервером"
private const val UNKNOW_ERROR_TOAST_MSG = "НЕИЗВЕСТНАЯ ОШИБКА"

class CatsPresenter(
    private val catsService: CatsService
) {
    private var _catsView: ICatsView? = null
    private lateinit var factJob: Job
    private val scope =
        PresenterScope(
            CoroutineName(COROUTINE_NAME)
                    + Dispatchers.Main +
                    SupervisorJob()
        )

    fun onInitComplete() = run {
        factJob = scope.launch {
            try {
                catsService.getCatFact()?.let { fact ->
                    _catsView?.populate(fact)
                }
            } catch (e: Exception) {
                checkException(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        if (::factJob.isInitialized) {
            factJob.cancel()
        }
    }

    private fun checkException(e: Exception) {
        when (e) {
            is CancellationException -> throw e
            is SocketTimeoutException -> showErrorToast(TIME_OUT_ERROR_TOAST_MSG)
            else -> {
                CrashMonitor.trackWarning()
                showErrorToast(e.message ?: UNKNOW_ERROR_TOAST_MSG)
            }
        }
    }

    private fun showErrorToast(message: String) {
        _catsView?.showErrorMessage(message)
    }
}