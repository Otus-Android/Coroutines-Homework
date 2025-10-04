package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.model.Model

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private var isPresenterMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        if (isPresenterMode) {
            catsPresenter = CatsPresenter(
                diContainer.service,
                diContainer.imagesService,
                ::onShowToast
            )

            view.presenter = catsPresenter
            catsPresenter.attachView(view)
            catsPresenter.onInitComplete()
        } else {
            val viewModel = CatsViewModel(
                diContainer.service,
                diContainer.imagesService,
                ::onShowToast
            )
            viewModel.onInitComplete()
            lifecycleScope.launch {
                viewModel.state.collect { state ->
                    when (state) {
                        is Result.Success<*> -> {
                            if (state.data is Model) {
                                view.populate(state.data)
                            }
                        }

                        is Result.Error -> {
                            onShowToast(state.msg)
                        }
                    }
                }
            }
        }
    }

    private fun onShowToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

}