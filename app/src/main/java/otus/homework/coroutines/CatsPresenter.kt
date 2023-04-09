package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: PicsService
) {

    private var _catsView: ICatsView? = null

    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {

        scope.launch {

            val deferredFact = async {
                try {
                    catsService.getCatFact()
                } catch (e: Exception) {
                    if (e is SocketTimeoutException) {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    } else {
                        CrashMonitor.trackWarning(e)
                        _catsView?.showToast(e.message)
                    }
                }
            }

            val deferredPic = async {
                try {
                    picsService.getPics().first().url
                } catch (e: Exception) {
                    if (e is SocketTimeoutException) {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    } else {
                        CrashMonitor.trackWarning(e)
                        _catsView?.showToast(e.message)
                    }
                }
            }

            val catFact = CatFact(
                factText = (deferredFact.await() as? Fact)?.text,
                imageUrl = deferredPic.await().toString()
            )
            _catsView?.populate(catFact)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        scope.cancel()
        _catsView = null
    }
}