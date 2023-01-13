package otus.homework.coroutines.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.R

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModelRealization(view)
//        presenterRealization(view)
    }

    /**
     * Реализация через ViewModel
     */
    private val viewModel by viewModels<CatsViewModel>(
        factoryProducer = { CatsViewModel.Factory }
    )

    private fun viewModelRealization(catsView: CatsView) {
        viewModel.catsState.observe(this) { result ->
            when (result) {
                is Result.Error -> catsView.showToast(result.error)
                is Result.Success -> catsView.populate(result.data)
                is Result.Loading -> catsView.loadingData(result.isLoading)
            }
        }
        catsView.button.setOnClickListener {
            viewModel.onInitComplete()
        }
    }

    /**
     * Реализация через презентер
     */

    private lateinit var catsPresenter: CatsPresenter

    private fun presenterRealization(catsView: CatsView) {
        catsPresenter = CatsPresenter(
            catsService = diContainer.catsService,
            imageService = diContainer.imageService
        )
        catsView.presenter = catsPresenter
        catsPresenter.attachView(catsView)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}