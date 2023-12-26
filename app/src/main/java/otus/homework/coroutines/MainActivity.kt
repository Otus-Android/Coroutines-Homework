package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.catsService, diContainer.factsService) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.onInitComplete()
        }

        lifecycleScope.launchWhenCreated {
            catsViewModel.catsFlow.collect { result ->
                when (result){
                    is Result.Success -> {
                        view.populate(result.value)
                    }
                    is Result.Error -> view.showMessage(result.message.toString())
                    else -> {}
                }
            }
        }
    }
}