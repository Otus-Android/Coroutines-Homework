package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        catsPresenter.getFactState.observe(this, catsFactObserver)
    }

    private val catsFactObserver = Observer<Result?>{
        when(it) {
            Empty -> {}
            is Error -> {
                when(it.t) {
                    is SocketTimeoutException -> showToast("Не удалось получить ответ от сервера")
                    else -> showToast(it.t?.message.toString())
                }
                catsPresenter.onMessageShown()
            }
            is Success -> {}
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(
            this, msg,
            Toast.LENGTH_LONG
        ).show()

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}