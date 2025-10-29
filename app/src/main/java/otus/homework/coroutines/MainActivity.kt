package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(CatsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return CatsViewModel(
                        diContainer.catsService,
                        diContainer.catsImageService,
                        getString(R.string.error_timeout)
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        })[CatsViewModel::class.java]

        viewModel.catsLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.data)
                is Result.Error -> view.showError(result.exception)
            }
        }

        view.loadAction = { viewModel.loadCatData() }
        viewModel.loadCatData()
    }
}