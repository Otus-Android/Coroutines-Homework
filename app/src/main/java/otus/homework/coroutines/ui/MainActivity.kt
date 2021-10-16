package otus.homework.coroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.R
import otus.homework.coroutines.utils.Result
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presenter.CatsPresenter

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    lateinit var viewModel: MainViewModel
    lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        setOnClickListeners(view)

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.coroutineScope)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModelFactory = MainViewModelFactory(diContainer.service)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)

        viewModel.handleEvent(MainEvent.GetNewFactEvent)

        viewModel.catModelLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.data)
                }
                is Result.Error -> {
                    view.showToast(result.errorMessage)
                }
            }
        }
    }

    private fun setOnClickListeners(view: CatsView) {
        view.findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.handleEvent(MainEvent.GetNewFactEvent)
        }
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        catsPresenter.cancelCurrentJob()
//        super.onStop()
//    }
}