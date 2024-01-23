package otus.homework.coroutines

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.cancellation.CancellationException

data class Model(
    val fact: String,
    val imageUrl: String
)

class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService
) {

    private var _catsView: ICatsView? = null

    private val scope = PresenterScope()

    fun onInitComplete() {
        scope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val imageData = async { imagesService.getImage() }

                _catsView?.populate(Model(
                    fact.await().fact,
                    imageData.await()[0].url
                ))
            } catch (e: CancellationException) {
                throw e
            } catch(e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showError(e)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        scope.cancel()
    }

    class PresenterScope() : CoroutineScope {
        override val coroutineContext: CoroutineContext = CoroutineName("CatsCoroutine") +
                Dispatchers.Main +
                Job() +
                CoroutineExceptionHandler { ctx, throwable ->
                    Log.e("pablok", "coroutine got throwable: $throwable")
                }
    }
}