package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels


class MainActivity : AppCompatActivity() {

    //    lateinit var catsPresenter: CatsPresenter
    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(
            diContainer.service,
            diContainer.imageService
        )
    }
    private val diContainer = DiContainer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.viewModel = catsViewModel
//        catsPresenter = CatsPresenter(diContainer.service, diContainer.imageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        catsViewModel.onViewInitializationComplete()
        catsViewModel.catsLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.data)
                }
                is Result.Error -> {
                    view.showExceptionMessage(result.message)
                }
            }
        }
    }


//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}


