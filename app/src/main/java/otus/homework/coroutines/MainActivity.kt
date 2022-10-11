package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

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

        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete(lifecycleScope)

        view.catViewModel = catsViewModel
        catsViewModel.catDescription.observe(this) {
            when(it) {
//                is Result.Loading -> view.setLoading(true)
                is Result.Error -> {
//                    view.setLoading(false)
                    val message = it.getMessage(this, getText(R.string.error_unknown))
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
//                    view.setLoading(false)
                    view.populate(it.data)
                }
//                null -> view.setLoading(false)
                is Result.Loading, null -> Unit
            }
        }
        catsViewModel.isLoading.observe(this) {
            view.setLoading(it ?: false)
        }
        catsViewModel.updateCat()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}