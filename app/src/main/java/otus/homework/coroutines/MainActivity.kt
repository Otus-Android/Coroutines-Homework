package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = ViewModelProvider(
            this,
            CatsViewModel.factory(diContainer.factService, diContainer.imageService)
        ).get(CatsViewModel::class.java)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.requestFactAndImage()
        }

        viewModel.factWithImageLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.value)
                }

                is Result.Error -> {
                    view.showToast(result.throwable.toString())
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
    }
}