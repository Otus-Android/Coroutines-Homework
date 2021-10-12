package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    
    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by viewModels { diContainer.catsViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        
        view.viewModel = catsViewModel
        catsViewModel.catsUiState.observe(this) { result ->
            when (result) {
                is Result.Success<CatsUiState> -> view.populate(result.data)
                is Result.Error -> view.showError(result.message)
            }
        }
    }
    
}