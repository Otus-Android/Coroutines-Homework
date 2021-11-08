package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val viewModel by viewModels<CatViewModel> { ViewModelFactory(diContainer.repository) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.action = viewModel::loadCat

        viewModel.data.observe(this) { result ->
            when (result) {
                is Success ->
                    view.populate(result.data)
                is Error ->
                    view.show(result.message)
            }
        }

//        catsPresenter = CatsPresenter(diContainer.repository)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        catsPresenter.onStop()
//        super.onStop()
//    }
}