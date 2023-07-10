package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    //    lateinit var catsPresenter: CatsPresenter
    lateinit var catViewModel: CatViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
//        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.serviceImage)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        catViewModel = ViewModelProvider(
            this,
            CatViewModel.CatsViewModelFactory(diContainer.serviceFact, diContainer.serviceImage)
        )[CatViewModel::class.java]

        view.viewModel = catViewModel
        observeCats(view)
        catViewModel.onInitComplete()
    }

    private fun observeCats(view: ICatsView) {
        catViewModel.catModel.observe(this, Observer {
            when (it) {
                is Result.Success -> view.populate(it.catData)
                is Result.Error -> view.showToast(it.throwable.message.toString())
                else -> {}
            }
        })
    }

    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        catsPresenter.onStop()
        super.onStop()
    }
}