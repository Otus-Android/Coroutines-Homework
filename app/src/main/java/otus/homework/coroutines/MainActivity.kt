package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by viewModels {
        ViewModelFactory(diContainer.factService, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = catsViewModel
        catsViewModel.catsLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error -> view.showErrorMessage(result.messageText)
            }
        }
    }
}