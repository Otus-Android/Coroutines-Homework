package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var requestJob: Job? = null
    private var _catsView: ICatsView? = null

    private val presenterScope = MainScope() + CoroutineName("CatsCoroutine")
    private val catsView get() = checkNotNull(_catsView)

    fun onInitComplete() {
        loadData()
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }

    fun onStart() {
        loadData()
    }

    fun onStop() {
        requestJob?.cancel()
        requestJob = null
    }

    private fun loadData() {
        if (requestJob != null && requestJob!!.isActive)
            return

        requestJob = presenterScope.launch {
            try {
                coroutineScope {
                    val fact = async(Dispatchers.IO) {
                        catsService.getCatFact()
                    }

                    val image = async(Dispatchers.IO) {
                        catsService.getCatImage()
                    }

                    catsView.populate(CatUiState(fact.await(), image.await()))
                }
            } catch (timeoutException: SocketTimeoutException) {
                catsView.showMessage("Не удалось получить ответ от сервера")
            } catch (exception: Exception) {
                catsView.showMessage("Упс, произошла ошибка: ${exception.message}")
                CrashMonitor.trackWarning(exception)
            }
        }
    }
}