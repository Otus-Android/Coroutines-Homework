package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.catService, diContainer.meowService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModel = ViewModelProvider(this)[CatsViewModel::class.java]
        viewModel.catsService = diContainer.catService
        viewModel.meowService = diContainer.meowService

        view.viewModel = viewModel
        viewModel.uiState.observe(this) {
            view.populate(it)
        }
        viewModel.errorState.observe(this) {
            view.showError(it)
        }
        viewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
//            catsPresenter.detachView()
            viewModel.release()
        }
        super.onStop()
    }
}