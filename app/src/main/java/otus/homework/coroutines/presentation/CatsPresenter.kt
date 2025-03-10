package otus.homework.coroutines.presentation

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.network.CatsImageService
import otus.homework.coroutines.data.network.CatsService
import otus.homework.coroutines.R
import otus.homework.coroutines.utils.CrashMonitor
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsImageService: CatsImageService,
    private val catsService: CatsService,
    private val applicationContext: Context,
) {

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var fetchJob: Job? = null

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        fetchJob = presenterScope.launch {
            try {
                val fact = catsService.getCatFact()
                val image = catsImageService.getCatImage()
//                _catsView?.render(
//                    CatsFactState(
//                        fact = fact.fact.orEmpty(),
//                        factLength = fact.length ?: 0,
//                        imageUrl = image[0].url.orEmpty(),
//                        isLoading = false
//                    )
//                )
            } catch (e: SocketTimeoutException) {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.connection_timeout_error),
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                Toast.makeText(
                    applicationContext,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
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
        fetchJob?.cancel()
    }
}