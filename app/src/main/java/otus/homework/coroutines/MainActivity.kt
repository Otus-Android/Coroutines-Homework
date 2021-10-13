package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.CatsViewModel.CatsViewModelFactory

class MainActivity : AppCompatActivity(), CatsView.Callback {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.callback = this

        catsViewModel.onMoreFacts()
        catsViewModel.result.observe(this, view::populate)
    }

    override fun onMoreFacts() {
        catsViewModel.onMoreFacts()
    }
}
