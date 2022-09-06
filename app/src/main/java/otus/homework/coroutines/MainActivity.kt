package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val catsViewModel: CatViewModel by viewModels {
        CatViewModel.ViewModelFactory(diContainer.service, diContainer.serviceForImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatView
        setContentView(view)

        view.viewModel = catsViewModel
        catsViewModel.onInitComplete()
        catsViewModel.getCatsLiveData.observe(this) { result ->
            when (result) {
                is Result.Success<FactAndImage> -> {
                    view.populate(result.data)
                }
                is Result.Error -> {
                    view.showToastMessage(result.messageText)
                }
            }
        }


//        catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceForImage)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}