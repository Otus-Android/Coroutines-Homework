package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        /*catsPresenter = CatsPresenter(diContainer.service, diContainer.imageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/

        catsViewModel = CatsViewModel(diContainer.service, diContainer.imageService)
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