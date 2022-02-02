package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val viewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        view.catsViewModel = viewModel

        viewModel.fetchCatsInfo()
        viewModel.loadCatsInfo().observe(this, {
            view.onDataChange(it)
        })
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}