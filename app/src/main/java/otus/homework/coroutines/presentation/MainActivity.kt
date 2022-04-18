package otus.homework.coroutines.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import otus.homework.coroutines.R

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by lazy { ViewModelProvider(this)[CatsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewTreeLifecycleOwner.set(window.decorView, this)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = viewModel

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.catResultFlow.collect { result ->
                    when (result) {
                        is Result.Loading -> view.displayLoading()
                        is Result.Error -> view.displayError(result.error)
                        is Result.Success -> view.displayData(result.data)
                    }
                }
            }
        }
    }
}