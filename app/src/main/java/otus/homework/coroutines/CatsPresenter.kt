package otus.homework.coroutines

import java.net.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val photoService: PhotoService,
    private val coroutineScope: CoroutineScope
) {


    private var _catsView: ICatsView? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        CrashMonitor.trackWarning()
    }

    fun onInitComplete() = coroutineScope.launch(handler) {
        try {
            coroutineScope {
                val list = awaitAll(
                    async { catsService.getCatFact() },
                    async { photoService.getPhoto() }
                )
                unionPopulate(list.first() as? Fact, list.last() as? Photo)
            }
        } catch (e: SocketTimeoutException) {
            _catsView?.showMessage(Message(stringId = R.string.socket_network_error))
        }
    }

    private fun unionPopulate(fact: Fact?, photo: Photo?) = fact?.let { localFact ->
        photo?.let { localPhoto ->
            _catsView?.populate(localFact, localPhoto)
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }
}