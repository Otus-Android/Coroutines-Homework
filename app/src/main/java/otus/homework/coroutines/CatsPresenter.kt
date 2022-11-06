package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope: CoroutineScope = CoroutineScope(
        context = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
    )

    fun onInitComplete() {
        val catImageUrlDeferred = presenterScope.async { catsService.getCatImageUrl() }
        val catFactDeferred = presenterScope.async { catsService.getCatFact() }

        presenterScope.launch {

            try {
                val catImageUrlResponse = catImageUrlDeferred.await()
                val catFactResponse = catFactDeferred.await()
                if (catImageUrlResponse.isSuccessful && catImageUrlResponse.body() != null &&
                    catFactResponse.isSuccessful && catFactResponse.body() != null ) {
                    val catPresentationModel = CatPresentationModel(
                        catFactMessage = catFactResponse.body()!!.fact,
                        catImageUrl = catImageUrlResponse.body()!!.fileUrl
                    )
                    _catsView?.populate(catPresentationModel)
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is CancellationException -> throw e
            is SocketTimeoutException ->
                _catsView?.showMessage(msg = "Server do not response")
            else -> {
                CrashMonitor.trackWarning(e)
                _catsView?.showMessage(msg = e.message!!)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}