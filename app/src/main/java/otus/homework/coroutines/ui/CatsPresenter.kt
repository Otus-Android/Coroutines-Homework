package otus.homework.coroutines.ui

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import otus.homework.coroutines.data.CatsService
import otus.homework.coroutines.util.CrashMonitor
import otus.homework.coroutines.data.PicsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: PicsService
) {

    private val coroutineScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val catFactResponse = catsService.getCatFact()
                    val picResponse = picsService.getPicture()
                    if (catFactResponse.isSuccessful && catFactResponse.body() != null &&
                        picResponse.isSuccessful && picResponse.body() != null
                    ) {
                        withContext(Dispatchers.Main) {
                            val model = CatsUiModel(
                                fact = catFactResponse.body()!!.fact,
                                pictureUrl = picResponse.body()!!.first().url,
                            )
                            _catsView?.populate(model)
                        }
                    }
                }
            } catch (t: Throwable) {
                if (t is SocketTimeoutException) {
                    _catsView?.showNetworkError()
                } else {
                    _catsView?.showError(t)
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun cancelJob() {
        coroutineScope.cancel()
    }
}