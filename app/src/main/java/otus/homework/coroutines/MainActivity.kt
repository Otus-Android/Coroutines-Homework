package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(diContainer.service)
    }

    private lateinit var catsView: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        catsView.button.setOnClickListener {
            viewModel.onInitComplete()
        }

        viewModel.catsState.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    catsView.populate(result.data)
                }
                is Result.Error -> {
                    catsView.showToast(result.message)
                }
                else -> {}
            }
        })

        if (savedInstanceState == null) {
            viewModel.onInitComplete()
        }
    }

}