package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.model.Result
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()
    private var viewModelFactory: CatsViewModelFactory = diContainer.viewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catsViewModel = ViewModelProvider(this,viewModelFactory).get(CatsViewModel::class.java)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceImage)
        view.presenter = catsPresenter

        findViewById<Button>(R.id.button).setOnClickListener { catsViewModel.onInitComplete() }

        catsViewModel.stateLiveData.observe(this, {
            if (it is Result.Success) {
                view.populate(it.cats)
            } else if (it is Result.Error) {
                showToastError(it.throwable, view)
            }
        })

        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()
    }

    private fun showToastError(throwable: Throwable, view: CatsView) {
        if (throwable is SocketTimeoutException) {
            view.showToastSomeException(throwable)
        } else {
            view.showToastTimeout(throwable)
        }
    }


    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}