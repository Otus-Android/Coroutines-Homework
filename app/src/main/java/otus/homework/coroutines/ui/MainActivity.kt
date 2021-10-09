package otus.homework.coroutines.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.ui.cats.CatsView
import otus.homework.coroutines.ui.cats.CatsViewModel
import otus.homework.coroutines.ui.cats.factory.CatsViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: CatsViewModel
    private lateinit var viewModelFactory: CatsViewModelFactory

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModelFactory = CatsViewModelFactory(diContainer.service)
        viewModel = ViewModelProvider(this, viewModelFactory)[CatsViewModel::class.java]
        view.viewModel = viewModel
        viewModel.attachView(view)
        viewModel.fetchCats()
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.detachView()
        }
        super.onStop()
    }
}