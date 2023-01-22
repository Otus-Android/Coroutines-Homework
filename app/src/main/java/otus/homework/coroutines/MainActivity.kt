package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.namespace.R

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        val viewModel by viewModels<CatsViewModel> {
            CatsViewModel.provideFactory(
                serviceRandomCatFact = diContainer.serviceRandomCatFact,
                serviceRandomCatImage = diContainer.serviceRandomCatImage
            )
        }
        view.viewModel = viewModel
        view.lifecycleScope = lifecycleScope
        view.subscribe()
        catsPresenter = CatsPresenter(diContainer.serviceRandomCatFact, diContainer.serviceRandomCatImage)
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}
