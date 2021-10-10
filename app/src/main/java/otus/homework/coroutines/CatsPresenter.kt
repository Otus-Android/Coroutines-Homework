package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {
    
    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var catDataJob: Job? = null
    private var _catsView: ICatsView? = null
    
    fun onInitComplete() = loadCatData()
    
    private fun loadCatData() = with(requireNotNull(_catsView)) {
        catDataJob?.cancel()
        catDataJob = scope.launch {
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
    ) = with(requireNotNull(_catsView)) {
        try {
            block(this@doSafely)
        } catch (e: CancellationException) {
            throw e
        } catch (e: SocketTimeoutException) {
            showError("Не удалось получить ответ от сервера")
        } catch (e: Exception) {
            showError(e.message ?: "Неизвестная ошибка")
            CrashMonitor.trackWarning()
        }
    }
    
    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }
    
    fun detachView() {
        catDataJob?.cancel()
        _catsView = null
    }
}