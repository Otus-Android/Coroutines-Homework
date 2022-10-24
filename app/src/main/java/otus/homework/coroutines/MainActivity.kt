package otus.homework.coroutines

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.PresenterScope
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val presenterScope = PresenterScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        showToastException(catsPresenter = catsPresenter, context = this)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        presenterScope.coroutineContext.job.cancel()
        super.onStop()
    }

    private fun showToastException(catsPresenter: CatsPresenter, context: Context) {
        presenterScope.launch {
            catsPresenter.errorsState.collectLatest { exceptions ->
                val message = if (exceptions is SocketTimeoutException) {
                    getString(R.string.socket_timeout_exception)
                } else {
                    CrashMonitor.trackWarning(exceptionMessage = exceptions.message.orEmpty())
                    exceptions.message
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}