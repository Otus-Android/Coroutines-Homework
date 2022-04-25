package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider


class MainActivity : AppCompatActivity() {

    private val diContainer by lazy {
        DiContainer()
    }

    private val viewModelFactory by lazy {
        CatsViewModelFactory(diContainer.service)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.fetchFactAndImage()
        }

        viewModel.facts.observe(this) { result ->
            when (result) {
                is Error -> {
                    view.showError(result.error)
                }
                is Success<FactAndImage> -> {
                    view.populate(result.fact)
                }
            }
        }

        viewModel.fetchFactAndImage()
    }
}