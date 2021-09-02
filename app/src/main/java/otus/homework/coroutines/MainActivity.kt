package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel = diContainer.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        /*catsPresenter = CatsPresenter(diContainer.service, diContainer.imageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/

        view.catsViewModel = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onViewInitializationComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            //catsPresenter.detachView()
        }
        super.onStop()
    }
}