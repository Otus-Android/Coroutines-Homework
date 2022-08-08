package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val photoService: PhotoService
) : CoroutineScope {

    private var job: Job = SupervisorJob()

    private val uiContext: CoroutineContext =
        CoroutineName("CatsPresenterCoroutine") + Dispatchers.Main

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        when (exception) {
            is SocketTimeoutException -> {
                _catsView?.showMessage(Message(stringId = R.string.socket_network_error))
            }
            else -> {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun onInitComplete() = launch(handler) {
        try {
            val factDeferred = async(Dispatchers.IO) {
                catsService.getCatFact()
            }
            val photoDeferred = async(Dispatchers.IO) {
                photoService.getPhoto()
            }
            unionPopulate(factDeferred.await(), photoDeferred.await())
        } catch (se: SocketTimeoutException) {
            _catsView?.showMessage(Message(stringId = R.string.socket_network_error))
        } catch (ex: Exception) {
            CrashMonitor.trackWarning()
        }
    }

    private fun unionPopulate(fact: Fact?, photo: Photo?) {
        fact?.let {
            photo?.let {
                _catsView?.populate(fact, photo)
            }
        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        job.cancelChildren()
    }
}