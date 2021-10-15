package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    private lateinit var mainView: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(mainView)

        catsPresenter = CatsPresenter(diContainer.service, diContainer.servicePicture)

        viewModel = CatsViewModelFactory(diContainer.service, diContainer.servicePicture).create(CatsViewModelImpl::class.java).apply {
            screenState.observe(this@MainActivity, Observer(::onScreenState))
        }

        catsPresenter.attachView(mainView)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun onScreenState(screenState: Result<CatUiModel, *>) {
        when (screenState) {
            is Result.Success -> {
                mainView.populate(screenState.value)
            }
            is Result.Error -> {
                when (screenState.error) {
                    is String -> mainView.showToast(screenState.error)
                    is Int -> mainView.showToast(screenState.error)
                }
            }
        }
    }
}