package otus.homework.coroutines.presenter

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CrashMonitor
import otus.homework.coroutines.model.usecase.CatsUseCase
import otus.homework.coroutines.view.ICatsView
import java.net.SocketTimeoutException

class CatsPresenter(
    private val catsUseCase: CatsUseCase,
    private val coroutineScope: CoroutineScope
) {

    private var _catsView: ICatsView? = null

    fun onInitComplete(context: Context) {
        coroutineScope.launch {
            try {
                _catsView?.populate(catsUseCase.getCatFact())
            } catch (e: SocketTimeoutException) {
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

    private fun showToast(
        context: Context,
        @StringRes message: Int
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}