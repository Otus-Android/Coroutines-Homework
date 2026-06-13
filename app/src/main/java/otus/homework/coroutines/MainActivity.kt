package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(
            catsService = diContainer.service,
            catsImageService = diContainer.imageService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = viewModel
        viewModel.onInitComplete()

        lifecycleScope.launch {
            viewModel.presentationState.collect { result ->
                when (result) {
                    is Result.Success -> {
                        view.populate(result.data)
                    }

                    is Result.Error -> {
                        view.showToast(result.message)
                    }

                    else -> {}
                }
            }
        }
    }
}