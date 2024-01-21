package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import otus.homework.coroutines.R.layout
import otus.homework.coroutines.Result.Error
import otus.homework.coroutines.Result.Success
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private var view: CatsView? = null

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(diContainer.catsService, diContainer.imagesService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(layout.activity_main, null) as CatsView
        setContentView(view)
        view?.viewModel = viewModel
        setupObservers()
        viewModel.onInitComplete()

//        catsPresenter = CatsPresenter(
//            catsService = diContainer.catsService,
//            imagesService = diContainer.imagesService
//        )
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

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        if (state != null) {
                            handleViewState(state)
                        }
                    }
                }
            }
        }
    }

    private fun handleViewState(state: Result) {
        when (state) {
            is Success<*> -> (state.data as? FactWithImage)?.let { view?.populate(it) }
            is Error -> handleError(state.error)
        }
    }

    private fun handleError(e: Throwable) {
        if (e is SocketTimeoutException) {
            view?.showToast("Не удалось получить ответ от сервера")
        } else {
            view?.showToast(e.message)
        }
    }
}