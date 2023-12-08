package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService
) {

    private var _catsView: ICatsView? = null
    private val scope = CatsScope()
    private var job: Job? = null

    fun onInitComplete() {
        job = scope.launch {
            try {
                val fact = async { catsService.getCatFact().fact }
                val image = async { imageService.getImage().body()!![0].url }
                _catsView?.populate(
                    (ImagedFact(fact.await(), image.await()))
                )
            } catch (e: SocketTimeoutException) {
                _catsView?.showAlert(R.string.exception_message)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _catsView?.showAlert("${e.message}")
                CrashMonitor.trackWarning(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        if (job?.isActive == true) {
            job?.cancel()
        }
    }
}
