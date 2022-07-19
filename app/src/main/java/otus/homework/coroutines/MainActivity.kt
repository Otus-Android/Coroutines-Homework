package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import otus.homework.coroutines.presentation.CatsViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.successLiveData.observe(this) {
            view.populate(it.model)
        }

        viewModel.errorLiveData.observe(this) {
            view.showError(it.errorMessage)
        }

        viewModel.loadingLiveData.observe(this) {
            view.showUILoading(it)
        }

        view.viewModel = viewModel
        viewModel.onInitComplete()
    }
}