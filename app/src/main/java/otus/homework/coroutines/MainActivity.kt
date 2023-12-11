package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by viewModels {
        catsViewModelFactory(diContainer.service, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.buttonOnClickDelegate = CatsView.LoaderDelegate { viewModel.loadCat() }
        lifecycleScope.launch {
            viewModel.state.collect { result ->
                when (result) {
                    is Result.Error -> view.showErrorToast(result.message)
                    is Result.Success<Cat> -> view.populate(result.data)
                    null -> Unit
                }
            }
        }

        if (savedInstanceState == null) viewModel.loadCat()
    }
}