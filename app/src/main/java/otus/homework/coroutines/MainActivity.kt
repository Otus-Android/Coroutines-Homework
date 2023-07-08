package otus.homework.coroutines

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import otus.homework.coroutines.R.layout
import otus.homework.coroutines.R.string
import otus.homework.coroutines.network.DiContainer
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.CatsView
import otus.homework.coroutines.utils.CrashMonitor
import otus.homework.coroutines.utils.PresenterScope
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(layout.activity_main, null) as CatsView
        setContentView(view)
        catsPresenter = CatsPresenter(diContainer)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        showToastException(catsPresenter = catsPresenter, context = this)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun showToastException(catsPresenter: CatsPresenter, context: Context) {
        lifecycleScope.launch {
            catsPresenter.errorsState.collectLatest { exceptions ->
                val message = if (exceptions is SocketTimeoutException) {
                    getString(string.socket_timeout_exception)
                } else {
                    CrashMonitor.trackWarning(exceptionMessage = exceptions.message.orEmpty())
                    exceptions.message
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}