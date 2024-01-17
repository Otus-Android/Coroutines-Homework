package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModel.Factory(diContainer.service, diContainer.imagesService)
    }
    private lateinit var view: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service, diContainer.imagesService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        // catsPresenter.onInitComplete() <-- закоментировал, чтобы релоада не было
        //                                    при каждом повороте экрана

        view.binding.button.setOnClickListener {
            viewModel.reload()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.resultFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleResult(it) }
            .launchIn(lifecycleScope)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun handleResult(result: Result<CatsUiModel>) {
        when (result) {
            is Result.Success -> view.populate(result.data)
            is Result.Error -> when (result.error) {
                is SocketTimeoutException -> view.toast(R.string.http_error_ste)
                else -> view.toast(R.string.http_error_template, result.error.message)
            }
        }
    }
}
