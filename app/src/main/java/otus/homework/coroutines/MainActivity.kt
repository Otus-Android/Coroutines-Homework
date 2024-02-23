package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import otus.homework.coroutines.utils.Result

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private lateinit var viewModel : CatsViewModel
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = CatsViewModel(diContainer.service, diContainer.image)
        view.viewModel = viewModel
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.catFact.collectLatest { result ->
                when (result) {
                    is Result.Success -> view.populateFact(result.data)
                    is Result.Error -> Toast.makeText(this@MainActivity, result.errorMessage, Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.catImage.collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        view.populateImage(result.data.url)
                    }
                    is Result.Error -> Toast.makeText(this@MainActivity, result.errorMessage, Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}