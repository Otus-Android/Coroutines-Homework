package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    //private lateinit var view: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.factService, diContainer.imageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        /*val viewModel: CatsViewModel by viewModels {
            MyViewModelFactory(diContainer.factService, diContainer.imageService)
        }

        view.viewModel = viewModel*/

        /*lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                when (it) {
                    is Result.Loading -> {
                        // showProgress
                    }
                    is Result.Success -> {
                        updateUi(it.data)
                    }
                    is Result.Error -> {
                        showError()
                    }
                }
            }
        }*/

    }

    /*private fun updateUi(fact: Fact) {
        view.populate(fact)
    }*/

    /*private fun showError() {
        Toast.makeText(
            applicationContext,
            "Не удалось получить ответ от сервера",
            LENGTH_LONG
        )
            .show()
    }*/

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}