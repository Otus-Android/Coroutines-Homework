package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


class CatsPresenter(
    private val catsService: CatsService
) {
    private val presenterScope =
        CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    private var job: Job? = null
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        job?.cancel()
        job = presenterScope.launch {
            try {
                coroutineScope {
                    val fact = async {
                        catsService.getCatFact()
                    }
                    val image = async {
                        catsService.getRandomImg(IMG_URL).getOrNull(0)
                            ?: throw NullPointerException()
                    }
                    val factResult = fact.await()
                    val imageResult = image.await()
                    _catsView?.populate(
                        catsInfo =
                            CatsInfo(
                                fact = factResult,
                                img = imageResult,
                            )
                    )
                }
            } catch (e: Exception) {
                handelError(e)
            }
        }
    }

    private fun handelError(e: Exception) {
        CrashMonitor.trackWarning()
        val errorMsg = when (e) {
            is java.net.SocketTimeoutException -> "Не удалось получить ответ от сервером"
            is CancellationException -> ""
            else -> e.message
        }
        errorMsg?.let { errorMsg ->
            if (errorMsg.isNotEmpty()) {
                _catsView?.showToast(errorMsg)
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun clear() {
        job?.cancel()
    }

    fun detachView() {
        _catsView = null
    }

    fun onDestroy() {
        presenterScope.cancel()
    }

    companion object {
        const val IMG_URL = "https://api.thecatapi.com/v1/images/search"
    }
}