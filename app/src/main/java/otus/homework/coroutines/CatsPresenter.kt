package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImage: CatsImage,
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    fun getCatFact() {
        presenterScope.launch {
            runCatching {
                launch {
                    catsService.getCatFact().apply {
                        if (isSuccessful) {
                            val body = body()
                            body ?: return@apply
                            _catsView?.populateFact(body)
                        }
                    }
                }
            }.onFailure { throwable ->
                CrashMonitor.trackWarning()
            }
        }

    }
    fun getCatImage() {
        presenterScope.launch {
            runCatching {
                launch {
                    catsImage.getCatImage().apply {
                        if (isSuccessful) {
                            val body = body()
                            body ?: return@apply
                            _catsView?.populateImage(body.first().url)
                        }
                    }
                }
            }.onFailure { throwable ->
                CrashMonitor.trackWarning()
            }
        }

    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}