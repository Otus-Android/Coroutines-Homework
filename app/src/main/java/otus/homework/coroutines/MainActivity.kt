package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private var catsViewModel = CatsViewModel(diContainer.serviceCatFact, diContainer.serviceCatImage)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = catsViewModel
        catsViewModel.result.observe(this) {
            when(it) {
                is CatsResult.Success<*> -> view.populate(it.result as CatModel)
                is CatsResult.Error -> view.makeToast(it.exc)
            }
        }
        catsViewModel.onInitComplete()
    }
}