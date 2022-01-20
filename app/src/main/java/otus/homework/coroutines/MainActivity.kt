package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.view.CatsView
import otus.homework.coroutines.presentation.view.CatsViewModel
import otus.homework.coroutines.presentation.view.CatsViewModelFactory

class MainActivity : AppCompatActivity() {

    private var catsPresenter: CatsPresenter?  = null
    private lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        // заменено на catsViewModel
//        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.servicePic)
        view.presenter = catsPresenter
        catsPresenter?.attachView(view)
        catsPresenter?.onInitComplete()

        catsViewModel = ViewModelProvider(this, CatsViewModelFactory(diContainer))[CatsViewModel::class.java]
        catsViewModel.state.observe(this) {
            view.process(it)
        }
        view.onClickAction = { catsViewModel.loadCats() }
        catsViewModel.start()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter?.detachView()
            catsViewModel.stop()
        }
        super.onStop()
    }
}