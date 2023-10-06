package otus.homework.coroutines.presentation

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatsService
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
) {
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineName("CatsCoroutine")
    )
    private var view: ICatsView? = null

    fun onInitComplete() {
        coroutineScope.launch {
            val fact = async(Dispatchers.IO) {
                catsService.getCatFact()
            }
            try {
                view?.populate(fact.await())
            } catch (e: SocketTimeoutException) {
                view?.showErrorToast(CatsService.TIMEOUT_MESSAGE)
            } catch (e: Exception) {
                e.message?.let { view?.showErrorToast(it) }
                CrashMonitor.trackWarning()
            }


        }
    }

    fun attachView(catsView: ICatsView) {
        view = catsView
    }

    fun detachView() {
        view = null
    }
}