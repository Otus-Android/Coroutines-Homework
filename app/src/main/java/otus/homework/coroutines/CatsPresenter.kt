package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.network.services.CatsService
import otus.homework.coroutines.network.services.RandomCatImageService
import otus.homework.coroutines.uientities.UiFactEntity
import java.net.SocketTimeoutException

class CatsPresenter(
    private val crashMonitor: CrashAnalyticManager,
    private val catsService: CatsService,
    private val randomCatImageService: RandomCatImageService,
    private val scope: CoroutineScope
) {
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        scope.launch {
            exceptionHandler {
                val uiFactEntity = getFactWithImage()
                _catsView?.populate(uiFactEntity = uiFactEntity)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun onStop() {
        cancelJobs()
    }

    fun detachView() {
        cancelJobs()
        _catsView = null
    }

    private suspend fun getFactWithImage() = coroutineScope {
        val catFact = async { catsService.getCatFact() }
        val randomCatImage = async { randomCatImageService.getRandomCatImage() }

        val factText = catFact.await().text
        val imageUrl = randomCatImage.await().imageUrl

        return@coroutineScope UiFactEntity(fact = factText, imageUrl = imageUrl)
    }

    private suspend fun exceptionHandler(callback: suspend () -> Unit) {
        try {
            callback.invoke()
        } catch (exception: CancellationException) {
            exception.printStackTrace()
            throw exception
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
            _catsView?.showServerError()
        } catch (exception: Exception) {
            exception.printStackTrace()
            crashMonitor.trackWarning()
            exception.message?.let {
                _catsView?.showDefaultError(message = it)
            }
        }
    }

    private fun cancelJobs() {
        scope.coroutineContext[Job]?.children?.forEach {
            it.cancel()
        }
    }
}