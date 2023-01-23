package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.api_services.DiContainer
import otus.homework.coroutines.api_services.ImContainer
import otus.homework.coroutines.data.Result
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val imContainer = ImContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service,imContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        catsPresenter.resultLoading.observe(this) {result ->
            showToastAboutFail(result)
        }
    }

    private fun showToastAboutFail(value: Result) {
        val message = if (value is Result.Error && value.exception is SocketTimeoutException) {
            this.resources.getString(R.string.no_server_response)
        } else {
            "${value.exception?.message}"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}