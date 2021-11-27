package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import otus.homework.coroutines.viewmodel.CatsViewModel
import otus.homework.coroutines.viewmodel.CatsViewModelFactory
import otus.homework.coroutines.viewmodel.CatsViewModelImpl
import otus.homework.coroutines.viewmodel.Result

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel

    init {
        viewModel = CatsViewModelFactory(diContainer.factService, diContainer.imageService).create(CatsViewModelImpl::class.java).apply {
            catResultState.observe(this@MainActivity, Observer(::onStateReceived))
        }
    }

    private var rootView: CatsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(rootView)

        rootView?.let{
            it.viewModel = viewModel
            viewModel.onInitComplete()
        }
    }

    private fun onStateReceived(state: Result){
        when(state){
            is Result.Success -> rootView?.populate(state.result)
            is Result.Error<*> -> {
                when (state.errorInfo){
                    is Int -> {rootView?.showMessage(state.errorInfo)}
                    is String -> {rootView?.showMessage(state.errorInfo)}
                    else -> {throw IllegalArgumentException("error info should be String or Int")}
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}