package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService
) {

    private var jobCat: Job? = null
    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))

    fun onInitComplete() {
        jobCat = presenterScope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { catsService.getCatImage() }
                val cat = Cat(fact.await(), image.await())
                _catsView?.populate(cat)
            } catch (e: SocketTimeoutException) {
                _catsView?.toasts("Не удалось получить ответ от сервера")
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        jobCat?.cancel()
    }
}