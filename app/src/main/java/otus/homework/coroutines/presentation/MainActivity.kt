package otus.homework.coroutines.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer

class MainActivity : AppCompatActivity() {

    //private lateinit var catsPresenter: CatsPresenter

    private val scope = MainScope()
    private val catsViewModel by lazy {
        ViewModelProvider(this)[CatsViewModel::class.java]
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        view.setRefreshListener {
            catsViewModel.onRefresh()
        }
        setContentView(view)
        catsViewModel.onCreate(
            catsService = diContainer.service,
            catsImageService = diContainer.catsImageService,
            applicationContext = applicationContext
        )
        catsViewModel.fetchCats()

        scope.launch {
            catsViewModel.state.collectLatest { newState ->
                view.render(newState)
            }
        }

//        catsPresenter = CatsPresenter(
//            catsImageService = diContainer.catsImageService,
//            catsService = diContainer.service,
//            applicationContext = applicationContext,
//        )
        //view.presenter = catsPresenter
        //catsPresenter.attachView(view)
        //catsPresenter.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//            catsPresenter.onStop()
//        }
//        super.onStop()
//    }
}