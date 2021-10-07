package otus.homework.coroutines.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.CatsView
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presenter.CatsPresenter

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service, diContainer.coroutineScope)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        catsPresenter.cancelCurrentJob()
        super.onStop()
    }
}