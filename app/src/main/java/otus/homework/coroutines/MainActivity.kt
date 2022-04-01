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
        catsViewModel.onInitComplete()

        initObserver()
    }

    private fun initObserver(){
        catsViewModel.fact.observe(this){
            val view = findViewById<CatsView>(R.id.main)
            view.populate(it)
        }
    }
}