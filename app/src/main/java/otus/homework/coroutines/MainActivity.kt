package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presentation.CatsViewModel

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels(
        factoryProducer = {
            CatsViewModel.Factory(
                catsService = diContainer.service,
                catImageService = diContainer.imageCatsService
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.successLiveData.observe(this) {
            view.populate(it.model)
        }

        viewModel.errorLiveData.observe(this) {
            view.showError(it.errorMessage)
        }

        viewModel.loadingLiveData.observe(this) {
            view.showUILoading(it)
        }

        view.viewModel = viewModel
        viewModel.onInitComplete()
    }
}