package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private var job: Job? = null

    private val presenterScope = CoroutineScope(
        Dispatchers.Main + CoroutineName("CatsCoroutine")
    )

    fun onInitComplete() {
        job?.cancel()
        job = presenterScope.launch {
            try {
                val factDeferred = async(Dispatchers.IO) {
                    catsService.getCatFact()
                }
                val imageDeferred = async(Dispatchers.IO) {
                    catsService.getCatImage().first()
                }
                val fact = factDeferred.await()
                val image = imageDeferred.await()

                val model = CatPresentationModel(
                    fact = fact.fact,
                    imageUrl = image.url
                )
                _catsView?.populate(model)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning(e)
                _catsView?.showToast(e.message ?: "Произошла ошибка")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    fun onStop() {
        job?.cancel()
        job = null
    }

}