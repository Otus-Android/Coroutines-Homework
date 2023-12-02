package otus.homework.coroutines.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.CatsView
import otus.homework.coroutines.CrashMonitor
import otus.homework.coroutines.R
import otus.homework.coroutines.data.DiContainer
import otus.homework.coroutines.domain.Result

class MainActivity : AppCompatActivity() {
    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val catViewModel by lazy {
        ViewModelProvider(this)[CatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        view.catViewModel = catViewModel

        catViewModel.result.observe(this) {
            when(it) {
                is Result.Success -> view.populate(it.catModel)
                is Result.Error -> CrashMonitor.trackWarning(this, it.exception)
            }
        }

        //catViewModel.onInitComplete()
    }

    override fun onStop() {
        catsPresenter.cancelAllJobs()
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}