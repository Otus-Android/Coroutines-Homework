package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    //    lateinit var catsPresenter: CatsPresenter
    private lateinit var catViewModel: CatViewModel

    private val diContainer = DiContainer()
    private val catsMapper = CatsMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        //        catsPresenter = CatsPresenter(
        //            catsService = diContainer.service,
        //            imageCatsService = diContainer.serviceImage,
        //            context = applicationContext,
        //            catMapper = catsMapper
        //        )
        //        view.presenter = catsPresenter
        //        catsPresenter.attachView(view)
        //        catsPresenter.onInitComplete()
        catViewModel = ViewModelProvider(
            this, MainFactory(
                application = application,
                imageCatsService = diContainer.serviceImage,
                catsService = diContainer.service,
                catMapper = catsMapper
            )
        )[CatViewModel::class.java]
        view.viewModel = catViewModel
        catViewModel.attachView(view)
        catViewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
//          catsPresenter.detachView()
            catViewModel.detachView()
        }
        super.onStop()
    }
}