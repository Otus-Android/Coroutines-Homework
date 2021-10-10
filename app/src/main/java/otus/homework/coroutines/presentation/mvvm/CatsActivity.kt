package otus.homework.coroutines.presentation.mvvm

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.databinding.CatFactBinding

class CatsActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CatsViewModel(diContainer.repository, diContainer.coroutineDispatchers) as T
            }
        })[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: CatFactBinding = CatFactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        if (savedInstanceState == null) {
            viewModel.loadCatRandomFact()
        }

        viewModel.error.observe(this, { Toast.makeText(this, it, Toast.LENGTH_LONG).show() })
    }
}