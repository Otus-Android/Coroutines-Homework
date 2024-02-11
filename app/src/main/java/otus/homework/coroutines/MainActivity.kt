package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(DiContainer.factService, DiContainer.imageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModel = ViewModelProvider(
            this, CatsViewModel.getViewModelFactory(
                DiContainer.factService,
                DiContainer.imageService
            )
        )[CatsViewModel::class.java]

        view.viewModel = viewModel
        viewModel.state.observe(this) {
            view.renderState(it)
        }
        viewModel.loadFact()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}