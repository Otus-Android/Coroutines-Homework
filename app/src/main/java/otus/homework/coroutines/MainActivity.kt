package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        catsViewModel = ViewModelProvider(this, Factory(diContainer.service))[CatsViewModel::class.java]
        catsViewModel.state.observe(this) { result ->
            catsView.handleNewState(result)
        }
        catsView.onClick = { catsViewModel.requestCatsInfo() }

        catsViewModel.requestCatsInfo()
    }
}