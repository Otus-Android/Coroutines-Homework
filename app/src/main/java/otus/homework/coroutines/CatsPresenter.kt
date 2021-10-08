package otus.homework.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val catsServicePicture: CatsServicePicture
) {

    private var _catsView: ICatsView? = null

    private var scope: CoroutineScope? = null

    private var loadFactsJob: Job? = null

    fun onInitComplete() {
        loadFactsJob?.cancel()
        loadFactsJob = scope?.launch {
            val catFact = async { getCatFact() }
            val catPicture = async { getCatPicture() }
            populate(catFact.await(), catPicture.await())
        }
    }

    private fun populate(catFact: Fact?, catPicture: CatPictureUrl?) {
        if (catFact != null && catPicture != null)
            _catsView?.populate(CatUiModel(catFact.text, catPicture.fileUrl))
    }

    private suspend fun getCatPicture() =
        loadWithCatching {
            loadCatPicture()
        }

    private suspend fun getCatFact() =
        loadWithCatching {
            loadCatFact()
        }

    private inline fun <T> loadWithCatching(body: () -> T): T? =
        try {
            body()
        } catch (e: SocketTimeoutException) {
            _catsView?.showToast(R.string.cat_fact_server_error)
            null
        } catch (e: Exception) {
            CrashMonitor.trackWarning()
            e.message?.let {
                _catsView?.showToast(it)
            }
            null
        }

    private suspend fun loadCatPicture() =
        catsServicePicture.getCatPicture()

    private suspend fun loadCatFact() =
        catsService.getCatFact()

    fun attachView(catsView: ICatsView) {
        scope = PresenterScope()
        _catsView = catsView
    }

    fun detachView() {
        scope?.cancel()
        scope = null
        _catsView = null
    }
}