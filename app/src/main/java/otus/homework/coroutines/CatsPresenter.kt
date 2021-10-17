package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val presenterScope: CoroutineScope
) {
    private var _catsView: ICatsView? = null
    private val catsView get() = checkNotNull(_catsView)

    fun onInitComplete() {
        presenterScope.launch {
            loadData()
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun stop() {
        presenterScope.cancel()
    }

    private suspend fun loadData() =
        try {
            coroutineScope {
                val fact = async {
                    catsService.getCatFact()
                }

                val image = async {
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