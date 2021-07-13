package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catsService = diContainer.service,
            imageService = diContainer.imageService
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        catsPresenter.error.observe(this) {
            when (it) {
                is ErrorState.SocketError -> showToast(getString(R.string.error_socket_text))
                is ErrorState.OtherError -> showToast(it.message)
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun showToast(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}