package otus.homework.coroutines

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val factsService: FactsService
) {

    private var _catsView: ICatsView? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, ex ->
        CrashMonitor.trackWarning(ex)
        _catsView?.showMessage(ex.message.toString())
    }

    fun onInitComplete() = CoroutineScope(Dispatchers.IO + SupervisorJob() + exceptionHandler).launch {
        try {
            val facts = async {
                factsService.getFact()
            }
            val cats = async {
                catsService.getCats()
            }
            val value = CatFact(facts.await().text, cats.await()[0].url)
            withContext(Dispatchers.Main){
                _catsView?.populate(value)
            }
        } catch (e: SocketTimeoutException) {
            _catsView?.showMessage(e.message.toString())
        }

    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}