package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val factory = CatsViewModelFactory(diContainer.catFactService, diContainer.randomCatService)
        viewModel = ViewModelProvider(this, factory)[CatsViewModel::class.java]

        viewModel.getResult().observe(this) { result ->
            when (result) {
                is Result.Success<*> -> {
                    view.populate(result.data as CatInfo)
                }
                is Result.Error -> {
                    view.showToast(result.errorMessage)
                }
            }
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitCompleted()
        }
    }
}