package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatViewModel by viewModels {
        CatViewModel.CatViewModelFactory(diContainer.serviceFact, diContainer.serviceImages)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

/*        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.serviceImages)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/

        viewModel.catsLiveData.observe(this) {
            when(it) {
                is CatViewModel.Result.Error -> view.showError(it.e)
                is CatViewModel.Result.Success -> view.populate(it.model)
            }
        }
        view.model = viewModel
        viewModel.updateData()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}