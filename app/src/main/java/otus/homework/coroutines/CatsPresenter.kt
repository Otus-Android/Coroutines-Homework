package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                withContext(Dispatchers.Main) {
                    _catsView?.populate(catInfo)
                }
            } catch (t: SocketTimeoutException) {
                withContext(Dispatchers.Main) {
                    _catsView?.showToast(SOCKET_TIMEOUT_EXCEPTION_MESSAGE)
                }

            } catch (t: Throwable) {
                CrashMonitor.trackWarning()
                withContext(Dispatchers.Main) {
                    _catsView?.showToast(t.message)
                }
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

