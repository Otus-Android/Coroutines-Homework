package otus.homework.coroutines

import kotlinx.coroutines.*
import otus.homework.coroutines.entities.CatEntity
import java.net.SocketTimeoutException

class CatPresenter(
    private val catsService: CatsService
) {
    private val presenterScope: PresenterScope by lazy { PresenterScope() }
    private var _catsView: CatsView? = null
    private var catJob: Job? = null

    fun onInitComplete() = presenterScope.launch {
        try {
            val catFact = async(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val catPhotoUrl = async(Dispatchers.IO) {
                catsService.getPhotoCat()
            }
            _catsView?.populate(
                CatEntity(
                    text = catFact.await().text,
                    catUrl = catPhotoUrl.await().fileUrl
                )
            )
        } catch (ce: CancellationException) {
            throw ce
        } catch (ste: SocketTimeoutException) {
            _catsView?.showToast("Не удалось получить ответ от сервера")
        } catch (e: Exception) {
            _catsView?.showToast(e.localizedMessage.orEmpty())
            CrashMonitor.trackWarning(this.javaClass.name, e.localizedMessage.orEmpty())
        }
    }


    fun attachView(catsView: CatsView) {
        _catsView = catsView
    }

    fun detachView() {
        catJob?.cancel()
        _catsView = null
    }
}