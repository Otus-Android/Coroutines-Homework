package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val applicationContext: Context,
    private val catsService: CatsService,
    private val picsService: PicsService
) {

    private val coroutineScope = CoroutineScope(CoroutineName("CatsCoroutine") + Dispatchers.Main)

    private var job: Job? = null

    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        job?.cancel()
        job = coroutineScope.launch {
            val factDiffered = async { catsService.getCatFact() }
            val picDiffered = async { picsService.getPic()[0] }
            try {
                val (fact, pic) = awaitAll(factDiffered, picDiffered)
                _catsView?.populate(
                    article = CatArticle(fact = fact as Fact, pic = pic as Pic)
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (e is SocketTimeoutException) {
                    Toast.makeText(
                        applicationContext,
                        "Не удалось получить ответ от сервера",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                    CrashMonitor.trackWarning(e)
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        job?.cancel()
        job = null
        _catsView = null
    }
}