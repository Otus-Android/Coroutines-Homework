package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), CatsView.Callback {

    private val diContainer = DiContainer()
    private val catsViewModel = diContainer.catsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.callback = this
        catsViewModel.result.observe(this, view::populate)
    }

    override fun onMoreFacts() {
        catsViewModel.onMoreFacts()
    }
}
