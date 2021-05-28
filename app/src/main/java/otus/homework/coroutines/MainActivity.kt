package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter


    private val diContainer = DiContainer()
    private val diContainerImage = DiContainerImage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val viewModel = ViewModelProvider(this).get(CatsViewModel::class.java)
        viewModel.catsService = diContainer.service
        viewModel.imageService = diContainerImage.imageService
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
//        catsPresenter = CatsPresenter(diContainer.service, diContainerImage.imageService)
//        view.viewModel = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}