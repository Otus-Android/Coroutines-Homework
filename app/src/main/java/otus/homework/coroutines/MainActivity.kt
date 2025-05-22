package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val catsViewModel: CatsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.service
        )
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.presenter = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsViewModel.detachView()
        }
        super.onStop()
    }
}