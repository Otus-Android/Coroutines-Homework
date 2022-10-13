package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var catsPresenter: CatsPresenter

    private val catsViewModel: CatsViewModel by viewModels() {
        CatsViewModelFactory(diContainer.service)
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        if (Constants.PRESENTER_VARIANT) {
            catsPresenter = CatsPresenter(diContainer.service)
            view.presenter = catsPresenter
            catsPresenter.attachView(view)
            catsPresenter.onInitComplete()
        } else {
            view.catViewModel = catsViewModel
            catsViewModel.catDescription.observe(this) {
                when (it) {
                    is Result.Error -> {
                        var message = it.getMessage(this)
                        if (message.isEmpty()) message = getText(R.string.error_unknown)
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                    is Result.Success -> {
                        view.populate(it.data)
                    }
                    is Result.Loading, null -> Unit
                }
            }

            catsViewModel.isLoading.observe(this) {
                view.setLoading(it ?: false)
            }

            catsViewModel.updateCat()
        }
    }

    override fun onStop() {
        if (isFinishing && ::catsPresenter.isInitialized) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}