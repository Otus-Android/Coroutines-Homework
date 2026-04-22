package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import otus.homework.reactivecats.CatsViewModel
import java.net.SocketTimeoutException
import kotlin.toString

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelsFactory(diContainer.service)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        view.onButtonClick = {
            viewModel.loadData()
        }

        setContentView(view)
        viewModel.state.observe(this) { result ->
            when (result) {
                is CatsViewModel.CatsResult.Success -> {
                    view.populate(result.data)
                }

                is CatsViewModel.CatsResult.Errors -> {
                    val message = when (result.e) {
                        is SocketTimeoutException -> getString(R.string.timeout_error_text)
                        else -> result.e.toString()
                    }

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}