package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var catsViewModel :CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsViewModel = ViewModelProvider(this, CatsViewModel.CatsViewModelFactory(diContainer.service))[CatsViewModel::class.java]

        view.viewModel = catsViewModel
        observeCats(view)
        catsViewModel.updateData()

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

    private fun observeCats(view: ICatsView) {
        catsViewModel.result?.observe(this, Observer {
            when(it) {
                is Result.Success<*> -> view.populate(it.result)
                is Result.Error -> view.toast(it.message)
                else -> {}
            }
        })
    }
    override fun onStop() {
        if (isFinishing) {
//            catsPresenter.detachView()
        }
        super.onStop()
    }
}