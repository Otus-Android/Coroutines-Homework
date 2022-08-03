package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModel.CatsViewModelProviderFactory(
            catsService = diContainer.service,
            catsPhotoService = diContainer.servicePhoto
        )
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.servicePhoto)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        lifecycleScope.launchWhenCreated {
            viewModel.screenState
                .collect {
                    when (it) {
                        is Result.Error -> {
                            view.showToast(it.message)
                        }
                        is Result.Success -> {
                            it.result?.let { it1 -> view.populate(it1) }
                        }
                        else -> {}
                    }
                }
        }
        viewModel.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}