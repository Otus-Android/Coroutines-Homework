package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()
    private var view: CatsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view?.initPresenter(catsPresenter)
        view?.let { catsPresenter.attachView(it) }
        catsPresenter.onInitComplete()

        catsViewModel = CatsViewModel(diContainer.service)
        view?.initViewModel(catsViewModel)
        catsViewModel.onInitComplete()

        catsViewModel.catsPopulationLD.observe(this) {
            view?.setViewContent(it)
        }

        catsViewModel.toastLD.observe(this) {
            view?.setToast(it)
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            view?.cancelJob()
        }
        super.onStop()
    }
}