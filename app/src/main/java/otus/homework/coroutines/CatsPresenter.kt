package otus.homework.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import otus.homework.coroutines.CrashMonitor.trackWarning
import otus.homework.coroutines.model.Model
import java.net.SocketTimeoutException


class CatsPresenter(
    private val catsService: CatsService,
    private val imagesService: ImagesService,
    private val onShowToast: (String?) -> Unit
) {

    private var _catsView: ICatsView? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + CoroutineName("CatsCoroutine"))
    fun onInitComplete() {
        presenterScope.launch {
            try {
                val fact = async { catsService.getCatFact() }
                val image = async { imagesService.getImage().firstOrNull() }

                _catsView?.populate(Model(fact.await(), image.await()?.url))
            } catch (e: SocketTimeoutException) {
                println("Caught $e")
                onShowToast("Не удалось получить ответ от сервера")

            } catch (e: Exception) {
                trackWarning(e.message)
                onShowToast(e.message)
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
        presenterScope.cancel()
    }
}