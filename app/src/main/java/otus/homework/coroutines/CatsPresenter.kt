package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketException
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    
    // Whole scope closed only on view detach to cancel all unnecessary jobs
    private val presenterScope = CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var dataLoadJob: Job? = null
    private var _catsView: ICatsView? = null
    
    fun onInitComplete() = launchCatDataLoading()
    
    fun onButtonClick() = launchCatDataLoading()
    
    private fun launchCatDataLoading() = withView {
        dataLoadJob?.cancel()
        dataLoadJob = launchSafely {
            populate(loadCatData())
        }
    }
    
    private suspend fun loadCatData(): CatsUiState = coroutineScope {
        val factDeferred = async {
            catsService.getCatFact()
        }
        val imageDeferred = async {
            catsService.getCatImage()
        }
        CatsUiState(factDeferred.await().text, imageDeferred.await().url)
    }
    
    private fun launchSafely(
        block: suspend CoroutineScope.() -> Unit
    ): Job = withView {
        presenterScope.launch(
            CoroutineExceptionHandler { _, e ->
                showError(e.message ?: "Неизвестная ошибка")
                CrashMonitor.trackWarning()
            }
        ) {
            try {
                block()
            } catch (e: SocketTimeoutException) {
                showError("Не удалось получить ответ от сервера")
            } catch (e: SocketException) {
                showError("Не удалось получить ответ от сервера")
            }
        }
    }
    
    private inline fun <T> withView(block: ICatsView.() -> T) = requireNotNull(_catsView).block()
    
    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }
    
    fun detachView() {
        dataLoadJob?.cancel()
        _catsView = null
    }
}