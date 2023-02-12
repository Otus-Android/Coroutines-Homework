package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        /*catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/

        viewModel = ViewModelProvider(this, AndroidViewModelFactory(application))[CatsViewModel::class.java]
        viewModel.catsService = diContainer.service
        view.viewModel = viewModel

        viewModel.result.observe(this){
            when(it){
                is Result.Success -> {
                    view.populate(it.data as Pair<Fact, Picture>)
                }
                is Result.Error -> {
                    when(it.message){
                        is String -> view.showToast(it.message as String)
                        is Int -> view.showToastFromRes(it.message as Int)
                    }
                }
            }
        }
        viewModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.onStop()
        }
        super.onStop()
    }
}