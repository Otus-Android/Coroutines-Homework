package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.welcometothemooncompanion.util.observe
import kotlinx.coroutines.flow.filterNotNull

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.factory(diContainer.catsImageService, diContainer.catFactService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        viewModel.uiState.filterNotNull().observe(this) {
            view.populate(it)
        }
        viewModel.uiEvents.observe(this) {
            view.showToast(it)
        }
        view.setClickListener {
            viewModel.load()
        }
    }
}