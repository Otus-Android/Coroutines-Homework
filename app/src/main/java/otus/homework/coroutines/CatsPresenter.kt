package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var catFactJob: Job? = null
    private var _catsView: ICatsView? = null

    fun onInitComplete() = loadCatFact()
    
    private fun loadCatFact() = with(requireNotNull(_catsView)) {
        catFactJob?.cancel()
        catFactJob = scope.launch {
            try {
                val fact = catsService.getCatFact()
                populate(fact)
            } catch (e: CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                showError("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                showError(e.message ?: "Неизвестная ошибка")
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catFactJob?.cancel()
        _catsView = null
    }
}