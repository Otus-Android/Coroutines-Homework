package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService,
    private val catPicturesService: CatPicturesService
) {

    private var _catsView: ICatsView? = null

    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        val factDef = presenterScope.async { catsService.getCatFact() }
        val picDef = presenterScope.async { catPicturesService.getCatPicture() }

        presenterScope.launch {
            val fact = fetchData { factDef.await().text }
            val picUrl = fetchData { picDef.await().picUrl }

            _catsView?.populate(CatData(fact, picUrl))
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }

    private inline fun <T> fetchData(action: () -> T): T? {
        return try {
            action.invoke()
        } catch (ex: SocketTimeoutException) {
            _catsView?.showSocketTimeoutExceptionToast()
            null
        } catch (ex: Exception) {
            CrashMonitor.trackWarning(CatsPresenter::class.simpleName, ex)
            _catsView?.showDefaultExceptionToast(ex.message)
            null
        }
    }

    class PresenterScope : CoroutineScope {
        override val coroutineContext: CoroutineContext = Dispatchers.Main + CoroutineName(COROUTINE_NAME) + SupervisorJob()
    }

    companion object {
        private const val COROUTINE_NAME = "CatsCoroutine"
    }
}