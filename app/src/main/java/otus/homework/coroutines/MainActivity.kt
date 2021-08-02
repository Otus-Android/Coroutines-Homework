package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.feature.CatsPresenter
import otus.homework.coroutines.feature.CatsView
import otus.homework.coroutines.feature.CatsViewModel
import otus.homework.coroutines.model.CatsData
import otus.homework.coroutines.model.Result.Error
import otus.homework.coroutines.model.Result.Success
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by viewModel()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceCatsImage, diContainer.dispatcherIO)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)

        swipeRefreshLayout = findViewById(R.id.catsView)

        swipeRefreshLayout.setOnRefreshListener {
            catsViewModel.onRefreshComplete()
        }

        catsViewModel.getFactResult().observe(this, {
            swipeRefreshLayout.isRefreshing = false
            when (it) {
                is Success<*> -> view.populate(it.data as CatsData)
                is Error -> toastError(it, view)
            }
        })
    }

    private fun toastError(it: Error, view: CatsView) {
        if (it.exception is SocketTimeoutException) view.toastSocketTimeoutException(it.exception)
        else view.toastSomeException(it.exception)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}