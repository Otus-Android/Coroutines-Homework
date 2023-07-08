package otus.homework.coroutines

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsService: CatsService,
    private val coroutineScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete(context: Context) {
        coroutineScope.launch {
            try {
                _catsView?.populate(getCatFact())
            }
            catch (e: SocketTimeoutException) {
                showToast(context, R.string.socket_timeout_error)
            } catch (e: Exception) {
                if (e is CancellationException) throw e

                CrashMonitor.trackWarning()
            }
        }
    }

    fun attachView(catsView: ICatsView) {
        _catsView = catsView
    }

    fun detachView() {
        _catsView = null
    }

    private suspend fun getCatFact(): Fact {
        return withContext(Dispatchers.IO) {
            catsService.getCatFact()
        }
    }

    private fun showToast(
        context: Context,
        @StringRes message: Int
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}