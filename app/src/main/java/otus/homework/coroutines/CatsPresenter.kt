package otus.homework.coroutines

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
        CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main.immediate)

    fun onInitComplete() {

        presenterScope.launch {
            try {
                val fact = catsService.getCatFact()

                val imageUrl = catsImageService.getCatImage().first()

                _catsView?.populate(
                    FactImageUI(
                        text = fact.fact,
                        url = imageUrl.url
                    )
                )
            } catch (e1: java.net.SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                _catsView?.showToast(e.toString())
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