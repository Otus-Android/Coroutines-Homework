package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        val deferredFact = presenterScope.async {
           try {
               catsService.getCatFact()
           } catch (e: SocketTimeoutException) {
               showExceptionMessage("Не удалось получить ответ от сервера.")
           } catch (e: Exception) {
               CrashMonitor.trackWarning(e)
               showExceptionMessage(e.message ?: "")
           }
        }

        val deferredImage = presenterScope.async {
            try {
                catsService.getCatImage()
            } catch (e: SocketTimeoutException) {
                showExceptionMessage("Не удалось получить ответ от сервера.")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                showExceptionMessage(e.message ?: "")
            }
        }

        presenterScope.launch {
            try {
                val fact = deferredFact.await() as Fact
                val catImage = (deferredImage.await() as List<*>).first() as CatImage
                _catsView?.populate(CatModel(fact, catImage))
            } catch (e: ClassCastException) {
                CrashMonitor.trackWarning(e)
                showExceptionMessage(e.message ?: "")
            }
        }
    }

    private fun showExceptionMessage(text: String) {
        _catsView?.showExceptionMessage(text)
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelAllJobs() {
        presenterScope.cancel()
    }
}