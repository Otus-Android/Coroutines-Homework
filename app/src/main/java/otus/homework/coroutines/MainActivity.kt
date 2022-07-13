package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModelFactory by lazy {
        with(diContainer) {
            CatsViewModelFactory(factService, photoService)
        }
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

        findViewById<Button>(R.id.button).setOnClickListener { viewModel.loadData() }

        viewModel.cats.observe(this) {
            when (it) {
                is Result.Error -> {
                    if (it.e is SocketTimeoutException) {
                        view.showServerResponseError()
                    } else {
                        it.e.message?.let { message ->
                            view.showError(message)
                        }
                    }
                }
                is Result.Success -> view.populate(it.response)
            }
        }
    }
}