package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private lateinit var viewModel: MainActivityViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//      Реализация Презентора --------------------
//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
//        -----------------------------------------

//        Реализация ViewModel---------------------
        viewModel = diContainer.viewModel
        view.viewModel = viewModel
        viewModel.onInitComplete()
        viewModel.catsData.observe(this, {
            when (it) {
                is Result.Success -> view.populate(it.data)
                is Result.Error -> view.showErrorToast("SocketTimeoutException")
            }
        })
//        ----------------------------------------
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            catsPresenter.stopJob()
        }
        super.onStop()
    }
}