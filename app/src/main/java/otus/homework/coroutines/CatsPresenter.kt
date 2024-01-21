package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catFactService: CatsService,
    private val randomCatService: RandomCatService
    ) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    fun onInitComplete() {

        job = PresenterScope().launch {
            try {
                val deferredFact = async { catFactService.getCatFact() }
                val deferredImage = async { randomCatService.getRandomCatImage() }

                val fact = deferredFact.await().fact
                val image = deferredImage.await().catImageUrl

                val catInfo = CatInfo(
                    catFactText = fact,
                    catImageUrl = image
                )
                _catsView?.populate(catInfo)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    println("Coroutine cancelled: ${e.message}")
                    throw e
                }
                if (e is SocketTimeoutException) {
                    _catsView?.showToast(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
                }
            } catch (t: Throwable) {
                CrashMonitor.trackWarning()
                _catsView?.showToast(t.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }

    companion object {
        const val SOCKET_TIMEOUT_EXCEPTION_MESSAGE = "Не удалось получить ответ от сервера"
    }
}

