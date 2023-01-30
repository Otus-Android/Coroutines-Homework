package otus.homework.coroutines

import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
) {

    private var _catsView: ICatsView? = null
    private val myScope = PresenterScope()

    fun onInitComplete() {
        myScope.launch {
            try {
                val fact = catsService.getCatFact()
                _catsView?.populate(fact)
            } catch (e: SocketTimeoutException) {
                _catsView?.showToast(R.string.failed_response)
            } catch (e: Exception) {
                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        myScope.cancel()
        _catsView = null
    }
}