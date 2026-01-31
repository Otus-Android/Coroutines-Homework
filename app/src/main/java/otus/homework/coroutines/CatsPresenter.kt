package otus.homework.coroutines

import android.content.res.Resources
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val resources: Resources,
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job = Job()

    fun load() {
        job = presenterScope.launch {
            runCatching {
                val facts = catsService.getCatFact()
                val imageUrl = catsService.loadImage().first().url

                CatFact(
                    fact = facts,
                    imageUrl = imageUrl,
                )
            }.onFailure { error ->
                if (error is CancellationException) throw error
                errorHandler(error)
            }.onSuccess {
                _catsView?.populate(it)
            }
        }
    }

    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val facts = catsService.getCatFact()
                _catsView?.populate(CatFact(facts))
            } catch (e: Exception) {
                errorHandler(e)
            }
        }
    }

    private fun errorHandler(e: Throwable) {
        if (e is SocketTimeoutException) {
            _catsView?.showToast(
                resources.getString(
                    R.string.socet_timeout_error,
                ),
            )
        } else {
            CrashMonitor.trackWarning()
            _catsView?.showToast(e.message.orEmpty())
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancel()
    }
}