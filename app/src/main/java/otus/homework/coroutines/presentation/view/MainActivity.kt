package otus.homework.coroutines.presentation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presentation.presentor.CatsPresenter
import otus.homework.coroutines.presentation.view_model.CatsViewModel
import otus.homework.coroutines.presentation.view_model.CatsViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catFactService = DiContainer.catFactService,
            catPictureService = DiContainer.catPictureService,
            resources = DiContainer.getResources(applicationContext)
        )

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        /*view.onClick = { catsViewModel.getCats() }
        catsViewModel.state.observe(this, { view.render(it) })
        catsViewModel.getCats()*/
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}