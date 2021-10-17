package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), CatsView.Callback {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service) }

    lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.callback = this

        // View Model
        catsViewModel.onMoreFacts()
        catsViewModel.result.observe(this, view::populate)

        // Presenter
//        catsPresenter = CatsPresenter(diContainer.service)
//        catsPresenter.attachView(view)
//        catsPresenter.onMoreFacts()
    }

    override fun onMoreFacts() {
        catsViewModel.onMoreFacts()
//        catsPresenter.onMoreFacts()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}
