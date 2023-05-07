package otus.homework.coroutines

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val imageService: ImageService,
    private val context: Context
) {

    private val presenterScope = PresenterScope()
    private var _catsView: ICatsView? = null

    fun onInitComplete() {
        val fact = presenterScope.async {
            catsService.getCatFact()
        }
        val img = presenterScope.async { imageService.getCatImg() }
        presenterScope.launch {
            try {
                _catsView?.populate(Data(fact.await().text, img.await()[0].url))
            } catch (e: java.net.SocketTimeoutException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.no_connect_server),
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
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