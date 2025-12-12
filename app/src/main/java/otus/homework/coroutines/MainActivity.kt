package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)


//            catsPresenter = CatsPresenter(
//                diContainer.service,
//                diContainer.imageService,
//                ::onShowToast
//            )
//            view.presenter = catsPresenter
//            catsPresenter.attachView(view)
//            catsPresenter.onInitComplete()

            val viewModel = CatsViewModel(
                diContainer.service,
                diContainer.imageService,
                ::onShowToast
            )
            viewModel.onInitComplete()
            view.viewModel = viewModel

            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    when (state) {
                        is Result.Success<*> -> {
                            if (state.data is CatModel)
                                view.populate(state.data)
                        }

                        is Result.Error -> {
                            onShowToast(state.msg)
                        }
                    }
                }
            }
    }

    override fun onStop() {
        if (isFinishing) {
            if(::catsPresenter.isInitialized) {
                catsPresenter.detachView()
                catsPresenter.onStop()
            }
        }
        super.onStop()
    }

    private fun onShowToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}