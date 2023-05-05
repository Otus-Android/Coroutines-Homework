package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catViewModel: CatViewModel by viewModels {
        CatViewModel.Factory(diContainer.service, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            catViewModel.getCatData()
        }

        catViewModel.result.observe(this) {
            when (it) {
                is Success -> view.populate(it.data)
                is Error -> view.showToast(it.msg)
            }
        }
    }
}