package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.catsService,
            diContainer.imageCatsService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        // Presenter
//        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.imageCatsService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        // ViewModel
        view.viewModel = catsViewModel
        catsViewModel.catsLiveData.observe(this) { result ->
            when (result) {
                is Success -> view.populate(result.cat)
                is Error -> view.showToast(result.message)
            }
        }

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}