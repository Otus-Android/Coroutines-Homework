package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    @Suppress("UNCHECKED_CAST")
    private val viewModel: CatsViewModel by viewModels {
        CatsViewModel.CatsViewModelProviderFactory(
            catsService = diContainer.service
        )
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        view.viewModel = viewModel
        viewModel.state.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error -> result.message?.let { view.showMessage(it) }
            }
        }
        viewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}