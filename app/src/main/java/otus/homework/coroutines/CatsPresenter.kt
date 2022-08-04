package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.api.CatsService
import otus.homework.coroutines.api.ImagesService
import otus.homework.coroutines.models.Content
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) {

    private var _catsView: ICatsView? = null
    private val scope = PresenterScope()
    private var job: Job? = null

    fun onInitComplete() {
        job = scope.launch {
            try {
                val fact = async {
                    catsService.getCatFact()
                }
                val image = async {
                    imagesService.getCatImage()
                }
                _catsView?.populate(
                    Content(
                        fact = fact.await(),
                        image = image.await()
                    )
                )
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: CancellationException) {
                CrashMonitor.trackWarning(e.message.orEmpty())
            } catch (e: Exception) {
                val message = e.message.orEmpty()
                CrashMonitor.trackWarning(message)
                _catsView?.showToast(message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.let { job ->
            if (job.isActive) {
                job.cancel()
            }
        }
    }
}
