package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private lateinit var viewModel: CatsViewModel

    private var _catsView: ICatsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        listenPresenterResult()

        viewModel = CatsViewModel(diContainer.service)
        _catsView = view
        listenViewModelResult()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        catsPresenter.cancel()
        viewModel.cancel()
        super.onStop()
    }

    private fun listenPresenterResult() {
        catsPresenter.errorResult.observe(this) { result ->
            if (result is Result.Error) {
                showErrorMessage()
            }
        }
    }

    private fun listenViewModelResult() {
        catsPresenter.errorResult.observe(this) { result ->
            when (result) {
                is Result.Error -> showErrorMessage()
                is Result.Success -> _catsView?.populate(result.data)
            }
        }
    }

    private fun showErrorMessage() {
        Toast.makeText(this, MESSAGE, Toast.LENGTH_SHORT).show()
    }

    private companion object {

        val MESSAGE: CharSequence = "Не удалось получить ответ от сервера"
    }
}
