package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private lateinit var catsViewModelFactory: CatsViewModelFactory
    private lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModelFactory = CatsViewModelFactory(diContainer.service, diContainer.imageService)
        catsViewModel = ViewModelProvider(this, catsViewModelFactory).get(CatsViewModel::class.java)
        view.viewModel = catsViewModel
        catsViewModel.catsPresentation.observe(this, {
            when (it) {
                is Result.Success -> {
                    view.populate(it.data)
                }
                is Result.Error -> {
                    view.showToast(it.message)
                }
            }
        })
        catsViewModel.onInitComplete()
    }

    override fun onStop() {
        super.onStop()
    }
}