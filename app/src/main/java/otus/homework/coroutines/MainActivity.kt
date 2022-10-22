package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels { CatsViewModel.Factory }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = viewModel
        viewModel.attachView(view)
        viewModel.onInitComplete(this)
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.detachView()
        }
        super.onStop()
    }
}