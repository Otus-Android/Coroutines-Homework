package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class MainActivity : AppCompatActivity() {
    private val viewModel: CatsViewModel by viewModels {
        val catsService = diContainer.service
        val catsServicePics = diContainer.serviceCatsPics
            CatsViewModelFactory(catsService, catsServicePics)
    }
    private var _catsView: ICatsView? = null

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        _catsView = view

        viewModel.onInitComplete()

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }
        viewModel.result.observe(this) { result ->
            when (result) {
                is CatsViewModel.Result.Success -> {
                    _catsView?.populate(result.data)
                }
                is CatsViewModel.Result.Error -> {
                    when (result.exception) {
                        is CancellationException -> throw result.exception
                        is SocketTimeoutException -> {
                            _catsView?.showServerErrorToast()
                        }
                        else -> {
                            _catsView?.showToast(result.exception.message.toString())
                            CrashMonitor.trackWarning()
                        }
                    }
                }
            }
        }
    }
    override fun onStop() {
        super.onStop()
    }
}