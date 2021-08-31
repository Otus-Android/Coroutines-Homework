package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import java.net.SocketTimeoutException
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels { ViewModelFactory(diContainer.catsService, diContainer.imagesService) }

    private lateinit var catsView: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        catsView = view
        setContentView(view)

        view.viewModel = viewModel
        viewModel.onInitComplete()

        viewModel.getFactState.observe(this, catsFactObserver)
    }

    private val catsFactObserver = Observer<Result?>{
        when(it) {
            Empty -> {}
            is Error -> {
                when(it.t) {
                    is SocketTimeoutException -> showToast("Не удалось получить ответ от сервера")
                    else -> showToast(it.t?.message.toString())
                }
                viewModel.onMessageShown()
            }
            is Success -> {
                catsView.populate(it.data)
            }
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(
            this, msg,
            Toast.LENGTH_LONG
        ).show()

}