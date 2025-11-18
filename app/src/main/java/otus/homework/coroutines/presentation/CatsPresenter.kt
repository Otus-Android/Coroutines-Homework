package otus.homework.coroutines.presentation

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.R
import otus.homework.coroutines.data.CatsFactService
import otus.homework.coroutines.data.CatsImageService
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsFactService: CatsFactService,
    private val catsImageService: CatsImageService,
    private val context: WeakReference<Context>,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var catsJob: Job? = null

    fun onInitComplete() {
        catsJob = presenterScope.launch {
            try {
                val imageDeferred = async { catsImageService.getCatImage().first() }
                val factDeferred = async { catsFactService.getCatFact() }

                val image = imageDeferred.await()
                val fact = factDeferred.await()
                _catsView?.populate(
                    CatsInfo(
                        fact = fact.fact,
                        imageUrl = image.url,
                    )
                )
            } catch (timeout: SocketTimeoutException) {
                Toast.makeText(context.get(), R.string.timeout_exception, Toast.LENGTH_SHORT).show()
            } catch (exception: Exception) {
                CrashMonitor.trackWarning()
                Toast.makeText(context.get(), exception.message, Toast.LENGTH_SHORT).show()
                ensureActive()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }


    fun catsJobCancel() {
        catsJob?.cancel()
    }

    fun detachView() {
        _catsView = null
    }
}
