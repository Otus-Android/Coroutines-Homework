package otus.homework.coroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.Result
import otus.homework.coroutines.R

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels<CatsViewModel> {
        CatsViewModel.Factory(
            catsFactService = diContainer.catsFactService,
            catsImageService = diContainer.catsImageService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.catsFactService, diContainer.catsImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModel.uiState.observe(this) { result ->
            when (result) {
                is Result.Success<*> -> view.populate(result.catUi as CatUi)
                is Result.Error -> view.showToastDefaultFailed(result.throwable)
            }
        }

        viewModel.fetchData()
        view.setOnButtonClickListener { viewModel.fetchData() }
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//            catsPresenter.cancelJob()
//        }
//        super.onStop()
//    }
}