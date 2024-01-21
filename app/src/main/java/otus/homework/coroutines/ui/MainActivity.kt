package otus.homework.coroutines.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presentation.CatsPresenter

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null, false) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            diContainer.factsService,
            diContainer.presenterScope
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}