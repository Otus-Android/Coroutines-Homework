package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presentation.CatModel
import otus.homework.coroutines.presentation.CatsViewModel
import otus.homework.coroutines.presentation.Result

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

        viewModel.resultLiveData.observe(this) {
            when (it) {
                is Result.Success<*> -> view.populate(it.model as CatModel)
                is Result.Loading -> view.showUILoading(it.isLoading)
                is Result.Error -> {
                    val errorMessage = it.errorMessage
                    if (errorMessage != null) {
                        view.showError(errorMessage)
                    }
                }
            }
        }

        view.viewModel = viewModel
        viewModel.onInitComplete()
    }
}