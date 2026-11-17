package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory(diContainer.service)
        )[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        view.presenter = viewModel
        viewModel.attachView(view)
        viewModel.updateFactAndImage()
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.detachView()
        }
        super.onStop()
    }
}