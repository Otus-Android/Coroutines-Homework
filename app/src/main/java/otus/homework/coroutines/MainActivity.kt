package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import otus.homework.coroutines.data.DiContainer
import otus.homework.coroutines.model.CatModel
import otus.homework.coroutines.view.CatsView

class MainActivity : AppCompatActivity() {

    private lateinit var catsViewModel: CatsViewModel
    private lateinit var catsView: CatsView
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        val factory = CatsViewModel.Factory(diContainer.service)
        catsViewModel = ViewModelProvider(this, factory)[CatsViewModel::class.java]
        catsView.viewModel = catsViewModel

        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            catsViewModel.resultObservable.collectLatest { result ->
                when (result) {
                    is Result.Success<*> -> {
                        if (result.data is CatModel) {
                            catsView.populate(result.data)
                        }
                    }
                    is Result.Error -> {
                        if (result.message is String) {
                            catsView.showToast(result.message)
                        } else if (result.message is Int) {
                            catsView.showToast(result.message)
                        }
                    }
                }
            }
        }
    }
}