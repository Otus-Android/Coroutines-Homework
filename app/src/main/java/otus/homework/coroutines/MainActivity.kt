package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val catsViewModel: CatsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CatsViewModel(diContainer.catsService, diContainer.catPicturesService) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.catPicturesService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        view.viewModel = catsViewModel
        val catsView = view as ICatsView
        catsViewModel.catData.observe(this) { result ->
            when (result) {
                is Result.Success -> catsView.populate(result.data)

                is Result.Error -> {
                    result.oldData?.let { catsView.populate(it) }
                    if (result.throwable is SocketTimeoutException) {
                        catsView.showSocketTimeoutExceptionToast()
                    } else {
                        catsView.showDefaultExceptionToast(result.throwable?.message)
                    }
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