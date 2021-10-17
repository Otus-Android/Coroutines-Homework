package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val requestButton = findViewById<Button>(R.id.button)

        val viewModel by viewModels<CatViewModel> { CatsViewModelFactory(diContainer.service) }

        requestButton.setOnClickListener {
            viewModel.loadInfo()
        }

        viewModel.catModel.observe(this) { result ->
            when (result) {
                is Result.Success<*> -> view.populate(result.value as CatModel)
                is Result.Error -> {
                    view.showToast(result.message)
                }
            }
        }

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        catsPresenter.onStop()
        super.onStop()
    }
}