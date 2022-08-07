package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(diContainer.factService, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.onInitComplete()
        }

        catsViewModel.result.observe(this) {
            when (it) {
                is Success -> view.populate(it.value)
                is Error -> view.showErrorMessage(it.message)
            }
        }
    }

}