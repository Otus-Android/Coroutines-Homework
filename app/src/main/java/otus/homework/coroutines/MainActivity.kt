package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel : CatsViewModel by viewModels {
        CatsViewModel.Factory(diContainer.factService, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getFactAndImage()
        }
        viewModel.getFactAndImage()
        observe(view)
    }

    private fun observe(view: CatsView) {
        viewModel.viewState.observe(this) { result ->
            when (result) {
                is Success -> view.populate(result.catData)
                is Error -> view.showToast(result.message)
                SocketExceptionError -> view.showToast(R.string.error_message)
            }
        }
    }

}