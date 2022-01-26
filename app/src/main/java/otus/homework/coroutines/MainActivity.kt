package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val viewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        view.viewModel = viewModel
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModel.data.observe(this) { result ->
            when (result) {
                is Result.Success<*> -> view.populate(result.data as Cat)
                is Result.Error -> {
                    view.toasts(result.error)
                }
            }
        }
        viewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            //catsPresenter.detachView()
            viewModel.detachView()
        }
        super.onStop()
    }
}