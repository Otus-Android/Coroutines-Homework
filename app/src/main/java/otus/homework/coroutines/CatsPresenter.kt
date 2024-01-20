package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val scope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    private lateinit var fact: Deferred<Fact>
    private lateinit var image: Deferred<Image>
    fun onInitComplete() {

        scope.launch {

            try {
                withContext(Dispatchers.IO) {
                    fact = async { catsService.getCatFact() }
                    image = async { imageService.getCatImage().first() }
                }

                _catsView?.populate(fact.await(), image.await())

            } catch (e: SocketTimeoutException) {
                _catsView?.toastError(e)
            } catch (e: CancellationException) {
                _catsView?.toastError(e)
            } catch (e: Exception) {
                _catsView?.toastError(e)
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
}