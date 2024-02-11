package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val catsImageService: CatsImageService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope =
        CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)

    fun onInitComplete() {

        presenterScope.launch {
            try {

                var fact = Fact("Empty fact", 0)
                var image = CatImage(null)

                val factJob = launch {
                    try {
                        fact = catsService.getCatFact()
                    } catch (e: Exception) {
                        handleError(e)
                    }
                }

                val imageJob = launch {
                    try {
                        image = catsImageService.getCatImage().first()
                    } catch (e: Exception) {
                        handleError(e)
                    }
                }

                factJob.join()
                imageJob.join()

                val factUI = FactImageUI(
                    text = fact.fact,
                    url = image.url
                )
                _catsView?.populate(
                    factUI
                )
            } catch (e: Exception) {
                handleError(e)
            }
        }

    }

    private fun handleError(e: Exception) {
        when (e) {
            is CancellationException -> {
                throw e
            }

            is java.net.SocketTimeoutException -> {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            }

            else -> {
                _catsView?.showToast(e.message.toString())
                CrashMonitor.trackWarning()
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