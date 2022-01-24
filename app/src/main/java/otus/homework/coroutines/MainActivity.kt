package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewmodel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel = CatsViewmodel(diContainer.service)
        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        view.viewModel = catsViewModel
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        //catsViewModel.getData()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            catsViewModel.stop()
        }
        super.onStop()
    }
}