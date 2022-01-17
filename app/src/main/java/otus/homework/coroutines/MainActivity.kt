package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.models.Cat
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        val viewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service) }

        viewModel.onInitComplete()
        viewModel.data.observe(this) { result ->
            when (result) {
                is Result.Success<*> -> view.populate(result.data as Cat)
                is Result.Error -> {
                    view.toasts(result.error!!)
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}