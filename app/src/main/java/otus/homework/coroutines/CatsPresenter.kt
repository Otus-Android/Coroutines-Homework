package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: NewCatsService,
    private val awsService: NewCatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()

    fun onInitComplete() {
        val fact = presenterScope.async(Dispatchers.IO) {
            catsService.getCatFact()
        }

        val picture = presenterScope.async(Dispatchers.IO) {
            awsService.getCatPicture()
        }

        presenterScope.launch {
            try {
                _catsView?.apply {
//                    populate(fact.await())
//                    populateImg(picture.await())
                }
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> _catsView?.showToast("Не удалось получить ответ от сервера")
                    is CancellationException -> _catsView?.showToast("Работа прервалась")
                    else -> _catsView?.showToast(e.message ?: "")
                }
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        presenterScope.cancel()
        _catsView = null
    }
}