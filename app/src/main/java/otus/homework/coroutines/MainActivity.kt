package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

//    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(diContainer.factService, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.state.observe(this) { result ->
            when (result) {
                is Result.Success<CatModel> -> view.populate(result.data)
                is Result.Error -> view.showToast(result.errorMsg)
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.fetchData()
        }

//        catsPresenter = CatsPresenter(diContainer.factService, diContainer.imageService)
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
