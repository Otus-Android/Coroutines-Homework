package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val toastEventListener: ToastEventListener
) {

    private var _catsView: ICatsView? = null

    private val presenterScope: CoroutineScope = CoroutineScope(
        context = Dispatchers.Main + CoroutineName("CatsCoroutine") + Job()
    )

    fun onInitComplete() {

        presenterScope.launch {

            var catImageUrl: String? = null
            var catFactText = "Something went wrong :("

            val imageJob = launch {
                try {
                    val response = catsService.getCatImageUrl()
                    if (response.isSuccessful && response.body() != null) {
                        catImageUrl = response.body()!!.fileUrl
                    }
                } catch (e: Exception) {
                    handleException(e)
                }
            }

            val factJob = launch {
                try {
                    val response = catsService.getCatFact()
                    if (response.isSuccessful && response.body() != null) {
                        catFactText = response.body()!!.text
                    }
                } catch (e: Exception) {
                    handleException(e)
                }
            }

            imageJob.join()
            factJob.join()

            if (catImageUrl != null) {
                val catPresentationModel = CatPresentationModel(
                    catFactMessage = catFactText,
                    catImageUrl = catImageUrl!!
                )
                _catsView?.populate(catPresentationModel)
            }
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is CancellationException -> throw e
            is SocketTimeoutException ->
                toastEventListener.onToastEvent(
                    message = "Server do not response"
                )
            else -> {
                CrashMonitor.trackWarning(e)
                toastEventListener.onToastEvent(
                    message = e.message!!
                )
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

    interface ToastEventListener {
        fun onToastEvent(message: String)
    }
}