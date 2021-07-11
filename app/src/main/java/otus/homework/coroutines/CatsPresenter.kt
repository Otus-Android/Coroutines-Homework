package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val job = Job()
    private val coroutineContext: CoroutineContext =
        Dispatchers.Main + job + CoroutineName("CatsCoroutine")
    private val customPresenterScope = CoroutineScope(coroutineContext)

    fun onInitComplete() {
        customPresenterScope.launch {
            getData()
        }
    }

    private suspend fun getData() {
        try {
            val fact = withContext(customPresenterScope.coroutineContext) {
               catsService.getCatFact()
            }
            val image = withContext(customPresenterScope.coroutineContext) {
                catsService.getCatImage()
            }
            if (
                fact.isSuccessful
                && image.isSuccessful
                && fact.body() != null
                && image.body() != null
            ) {
                val data = CustomCatPresentationModel(
                    checkNotNull(fact.body())[0].fact,
                    checkNotNull(image.body()).file
                )
                _catsView?.populate(data)
            } else CrashMonitor.trackWarning(fact.message())


        } catch (exception: SocketTimeoutException) {
            _catsView?.showErrorToast("timeout exception")
        } catch (exception: Exception) {
            CrashMonitor.trackWarning(exception.message)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun stopJob(){
        job.complete()
    }


}