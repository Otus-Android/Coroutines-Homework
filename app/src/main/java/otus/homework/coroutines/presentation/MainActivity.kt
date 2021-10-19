package otus.homework.coroutines.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.DiContainer

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        /**
         * viewModel
         */
        val viewModelFactory = ViewModelFactory(diContainer.imageService, diContainer.factService)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(CatViewModel::class.java)

        /**
         * presenter
         */
        catsPresenter = CatsPresenter(diContainer.imageService, diContainer.factService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {

        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}