package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    
    // Whole scope closed only on view detach to cancel all unnecessary jobs
    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var catDataJob: Job? = null
    private var _catsView: ICatsView? = null
    
    fun onInitComplete() = loadCatData()
    
    fun onButtonClick() = loadCatData()
    
    private fun loadCatData() = withView {
        catDataJob?.cancel() // Cancel extra network calls on quick button clicks
        catDataJob = scope.launch(CoroutineExceptionHandler { _, e ->
            showError(e.message ?: "Неизвестная ошибка")
            CrashMonitor.trackWarning()
        }) {
            val factDeferred = async {
                catsService.getCatFact()
            }
            val imageDeferred = async {
                catsService.getCatImage()
            }
            doSafely {
                populate(CatsUiState(factDeferred.await().text, imageDeferred.await().url))
            }
        }
    }
    
    private suspend fun CoroutineScope.doSafely(
        block: suspend CoroutineScope.() -> Unit
    ) = withView {
        try {
            block(this@doSafely)
        } catch (e: SocketTimeoutException) {
            showError("Не удалось получить ответ от сервера")
        }
    }
    
    private inline fun withView(block: ICatsView.() -> Unit) = requireNotNull(_catsView).block()
    
    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }
    
    fun detachView() {
        catDataJob?.cancel()
        _catsView = null
    }
}