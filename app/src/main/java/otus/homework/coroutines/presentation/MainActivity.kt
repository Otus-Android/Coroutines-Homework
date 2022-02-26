package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.R
import otus.homework.coroutines.view.CatsView

class MainActivity : AppCompatActivity() {

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(diContainer.service, diContainer.imageService)
    }

    private lateinit var catsPresenter: CatsPresenter
    private lateinit var view: CatsView

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service, diContainer.imageService)
        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        observeData()
        view.buttonClick { catsViewModel.onInitComplete() }
    }

    private fun observeData() {
        catsViewModel.result.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.data)
                }
                is Result.Error -> {
                    view.showError(result.error.message)
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