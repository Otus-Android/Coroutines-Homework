package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val viewModel by viewModels<CatsViewModel>(
        factoryProducer = { CatsViewModel.Factory(diContainer.service) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.load()

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)

        view.onClick = { viewModel.load() }
        /** код для варианта с презентером
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
         */
        viewModel.uiModelFlow
            .onEach { result ->
                when (result) {
                    is Result.Error -> view.bindError(result.message)
                    Result.Loading -> view.bindLoading()
                    is Result.Success -> view.bindSuccess(result.uiModel)
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}