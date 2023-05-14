package otus.homework.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val picsService: PicsService
) {

    private var _catsView: ICatsView? = null

    private val scope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine") + Job())

    fun onInitComplete() {

        scope.launch {

            try {
                val deferredFact = async { catsService.getCatFact() }
                val deferredPic = async { picsService.getPics().first().url }
                val catFact = CatFact(
                    factText = (deferredFact.await() as? Fact)?.text,
                    imageUrl = deferredPic.await()
                )
                _catsView?.populate(catFact)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> throw e

                    is SocketTimeoutException -> {
                        _catsView?.showToast("Не удалось получить ответ от сервером")
                    }

                    else -> {
                        CrashMonitor.trackWarning(e)
                        _catsView?.showToast(e.message)
                    }
                }
            }
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