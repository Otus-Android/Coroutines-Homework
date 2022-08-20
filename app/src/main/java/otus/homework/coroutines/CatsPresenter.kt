package otus.homework.coroutines

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val photoService: PhotoService
) {


    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() = coroutineScope.launch(handler) {
        try {
            val fact = catsService.getCatFact()
            val photo = photoService.getPhoto()
            unionPopulate(fact, photo)
        } catch (se: IOException) {
            _catsView?.showMessage(Message(stringId = R.string.socket_network_error))
        }
    }

    private fun unionPopulate(fact: Fact, photo: Photo) = _catsView?.populate(fact, photo)


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        coroutineScope.coroutineContext.cancelChildren()
    }
}

public val CatsPresenter.coroutineScope: CoroutineScope
    get() = CoroutineScope(CoroutineName("CatsPresenterCoroutine") + Dispatchers.Main + SupervisorJob())
