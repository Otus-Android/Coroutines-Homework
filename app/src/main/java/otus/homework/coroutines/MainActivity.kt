package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.Result
import kotlin.getValue

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CatsViewModel.provideFactory(diContainer.catsService, diContainer.catsImageService)
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.populate()

        view.setOnButtonClick {
            viewModel.populate()


//        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.catsImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        }


        val catObserver = Observer <Result> { catResult ->
            when(catResult) {
                is Result.Error -> view.showErrorToast(catResult.throwable.message.toString())
                is Result.Success<*> -> view.populate(catResult.catData as Cat)


            }

        }
        viewModel.uiState.observe(this, catObserver)

//    fun onStart() {
//        super.onStart()
//       viewModel.populate()
//    }
//
//    override fun onStop() {
//        super.onStop()
//    }
    }
}


