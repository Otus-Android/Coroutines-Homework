package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(diContainer.factService, diContainer.imgService)
    }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.resourceProvider = ResourceProvider(applicationContext)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getCatFact()
        }

        viewModel.getCatEntity().observe(this) {
            it?.let {
                when (it) {
                    is Result.Success -> {
                        view.populate(it.entity)
                    }

                    is Result.Error -> {
                        view.showError(it.message)
                    }
                }
            }
        }
    }
}
