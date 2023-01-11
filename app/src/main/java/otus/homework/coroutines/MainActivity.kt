package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels { CatsViewModel.Factory }

//    lateinit var catsPresenter: CatsPresenter
//    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
        view.viewModel = viewModel
        viewModel.onInitComplete()
        viewModel._catsData.observe(this) {
            when (it) {
                is Result.Success -> view.populate(it.item)
                is Result.Error -> {
                    val message = when (it.exception) {
                        is java.net.SocketTimeoutException -> {
                            "Не удалось получить ответ от сервера"
                        }
                        else -> {
                            CrashMonitor.trackWarning(it.exception.message)
                            it.exception.message ?: ""
                        }
                    }
                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
//            catsPresenter.getCatFact?.cancel()
            viewModel.getCatFact?.cancel()
        }
        super.onStop()
    }
}