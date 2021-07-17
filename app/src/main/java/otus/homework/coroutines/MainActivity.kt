package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel by viewModels<CatsViewModel> { diContainer.viewModelProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = (layoutInflater.inflate(R.layout.activity_main, null) as CatsView).apply {
            setViewModel(viewModel, this@MainActivity)
        }
        setContentView(view)
        savedInstanceState ?: viewModel.fetchCatsModel()
    }
}