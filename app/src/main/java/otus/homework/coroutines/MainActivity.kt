package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = CatsViewModel(diContainer.factService, diContainer.meowService)
        view.viewModel = viewModel
        viewModel.result.observe(this) { result ->
            view.populate(result)
        }
        viewModel.onInitComplete()


        /*catsPresenter = CatsPresenter(diContainer.factService, diContainer.meowService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/
    }

    override fun onStop() {
        /*if (isFinishing) {
            catsPresenter.detachView()
        }*/
        super.onStop()
    }
}