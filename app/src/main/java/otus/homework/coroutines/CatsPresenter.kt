package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
) {

    private var presenterScope: CoroutineScope? = null
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope?.launch {
            try {
                val fact: Fact
                val images: List<Image>
                val factDeferred = async { catsService.getCatFact() }
                val imagesDeferred = async { imagesService.getImages() }
                fact = factDeferred.await()
                images = imagesDeferred.await()
                _catsView?.populate(CatsUiModel(fact, images.first()))
            } catch (ce: CancellationException) {
                throw ce
            } catch (ste: java.net.SocketTimeoutException) {
                _catsView?.toast(R.string.http_error_ste)
            } catch (exception: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.toast(R.string.http_error_template, exception.message)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
        presenterScope = CoroutineScope(
            Dispatchers.Main + CoroutineName("CatsCoroutine") + SupervisorJob()
        )
    }

    fun detachView() {
        presenterScope?.cancel()
        _catsView = null
    }
}
