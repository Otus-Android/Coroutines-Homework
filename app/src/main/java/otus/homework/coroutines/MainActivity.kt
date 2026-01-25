package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var catsView: CatsView

    private val catsViewModel: CatsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CatsViewModel(
                    DiContainer.catFactService,
                    DiContainer.catImageService
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        catsView.findViewById<android.widget.Button>(R.id.button).setOnClickListener {
            catsViewModel.loadCatData()
        }

        observeViewModel()
        catsViewModel.loadCatData()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            catsViewModel.catsState.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        catsView.showLoading()
                    }
                    is Result.Success -> {
                        catsView.populate(result.data)
                    }
                    is Result.Error -> {
                        catsView.showError(result.message)
                    }
                }
            }
        }
    }
}