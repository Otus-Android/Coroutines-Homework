package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private lateinit var catsViewModelFactory: CatsViewModelFactory
    private lateinit var catsViewModel: CatsViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModelFactory = CatsViewModelFactory(diContainer.service, diContainer.servicePicture)
        catsViewModel = ViewModelProvider(this, catsViewModelFactory).get(CatsViewModel::class.java)
        view.viewModel = catsViewModel
        catsViewModel.state.observe(this, {
            when (it) {
                is Result.Success -> {
                    view.plant(it.data)
                }
                is Result.Error -> {
                    view.toast(it.message)
                }
            }
        })
        catsViewModel.onInitComplete()



//        catsPresenter = CatsPresenter(diContainer.service, diContainer.servicePicture)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

}