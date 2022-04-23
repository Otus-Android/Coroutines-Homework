package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.network.CatDataRepository
import otus.homework.coroutines.viewmodel_variant.CatsViewModel
import otus.homework.coroutines.viewmodel_variant.CatsViewModelFactory
import otus.homework.coroutines.viewmodel_variant.Success
import otus.homework.coroutines.viewmodel_variant.Error
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(
            catDataRepository = CatDataRepository(
                diContainer.factService,
                diContainer.imageService
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.viewModel = viewModel
        viewModel.onInitComplete()
        viewModel.result.observe(this) { result ->
            when(result) {
                is Success -> view.populate(result.data)
                is Error -> view.showErrorToast(R.string.network_error)
            }
        }
    }
}