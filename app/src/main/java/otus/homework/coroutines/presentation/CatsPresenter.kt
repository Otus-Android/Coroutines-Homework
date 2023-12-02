package otus.homework.coroutines.presentation

import kotlinx.coroutines.*
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.ICatsView
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.domain.CatImage
import otus.homework.coroutines.domain.CatModel
import otus.homework.coroutines.domain.Fact
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        presenterScope.launch {

            val deferredFact = async(Dispatchers.IO) {
                try {
                    catsService.getCatFact()
                } catch (e: SocketTimeoutException) {
                    _catsView?.showExceptionMessage("Не удалось получить ответ от сервера.")
                    showExceptionMessage("Не удалось получить ответ от сервера.")
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    showExceptionMessage(e.message ?: "")
                }
            }

            val deferredImage = async(Dispatchers.IO) {
                try {
                    catsService.getCatImage()
                } catch (e: SocketTimeoutException) {
                    showExceptionMessage("Не удалось получить ответ от сервера.")
                } catch (e: Exception) {
                    CrashMonitor.trackWarning(e)
                    showExceptionMessage(e.message ?: "")
                }
            }

            try {
                val fact = deferredFact.await() as Fact
                val catImage = (deferredImage.await() as List<CatImage>).first()
                _catsView?.populate(CatModel(fact, catImage))
            } catch (e: ClassCastException) {
                CrashMonitor.trackWarning(e)
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