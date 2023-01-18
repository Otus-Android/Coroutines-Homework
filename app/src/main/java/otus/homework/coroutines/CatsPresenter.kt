package otus.homework.coroutines

import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext


class CatsPresenter(
    private val catsService: CatsService,
    private val serviceImage: CatsService
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = PresenterScope()
    private var job: Job? = null


    fun onInitComplete() {
        job = presenterScope.launch {
            try {
                val jobImage = async {
                    val catImage = serviceImage.getCatImage()
                    _catsView?.populate(catImage)
                }
                val jobFact = async {
                    val fact = catsService.getCatFact()
                    _catsView?.populate(fact)
                }
                jobImage.join()
                jobFact.join()
            } catch (e: SocketTimeoutException) {
                _catsView?.catsMessage("Не удалось получить ответ от сервера")
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.catsMessage(e.message.toString())
            }

        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job?.cancel()
    }
}


class PresenterScope() : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main +
            CoroutineName("CatsCoroutine") +
            Job()
}
