package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private val viewModel: MainActivityViewModel =
    ViewModelProvider(this, MainActivityViewModelFactory(diContainer.service)
    ).get(MainActivityViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//      Реализация Презентора --------------------
//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
//        -----------------------------------------

//        Реализация ViewModel---------------------
        view.viewModel = viewModel
        viewModel.onInitComplete()
        viewModel.catsData.observe(this, {
            when (it) {
                is Result.Success -> view.populate(it.data)
                is Result.Error -> {
                    if (it.exception is SocketTimeoutException) {
                        view.showErrorToast("Не удалось получить ответ от сервера")
                    } else {
                        view.showErrorToast(it.exception.message.toString())
                    }
                }
            }
        })
//        ----------------------------------------
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            catsPresenter.stopJob()
        }
        super.onStop()
    }
}