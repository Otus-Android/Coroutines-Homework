package otus.homework.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val awsService: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private var catJob: Job? = null

    fun onInitComplete() {
        val fact = presenterScope.async(Dispatchers.IO) {
            catsService.getCatFact()
        }

        val picture = presenterScope.async(Dispatchers.IO) {
            awsService.getCatPicture()
        }

        catJob = presenterScope.launch {
            try {
                _catsView?.apply {
                    populate(fact.await())
                    populateImg(picture.await())
                }
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                e.message?.let {
                    _catsView?.showToast(it)
                    CrashMonitor.trackWarning()
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catJob?.cancel()
        _catsView = null
    }
}