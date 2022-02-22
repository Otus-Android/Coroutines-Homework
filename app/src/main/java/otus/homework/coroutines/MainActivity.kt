package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val catsViewModel: CatsViewModel by viewModels()

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel.catsService = diContainer.service
        view.viewModel = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) catsViewModel.onViewStop()
        super.onStop()
    }
}