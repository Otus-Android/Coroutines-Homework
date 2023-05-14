package otus.homework.coroutines

import android.content.Context
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val context: Context
) {

    private val presenterScope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        presenterScope.launch {
            val fact = async {
                catsService.getCatFact()
            }
            val img = async { imageService.getCatImg() }

            try {
                _catsView?.populate(Data(fact.await().text, img.await()[0].url))
            } catch (e: java.net.SocketTimeoutException) {
                _catsView?.showError(context.getString(R.string.no_connect_server))
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                _catsView?.showError(e.message.toString())
                if (e is CancellationException) throw e
            }

        }
    }


    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
        presenterScope.cancel()
    }
}