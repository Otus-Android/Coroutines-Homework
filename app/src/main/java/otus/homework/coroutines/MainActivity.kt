package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val diContainerImage = DiContainerImage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val viewModel = ViewModelProvider(
            this, ViewModelFactory(diContainer.service, diContainerImage.imageService)
        ).get(CatsViewModel::class.java)

        view.viewModel = viewModel
        viewModel.observableCat().observe(this) {
            it?.let {
                when (it) {
                    is Result.Success<*> -> {
                        when (it.result) {
                            is Fact -> view.populate(it.result)
                            is CatImage -> view.populateImage(it.result)
                        }
                    }
                    is Result.Error -> view.showError(it.errorMessage)
                }
            }
        }
    }

}