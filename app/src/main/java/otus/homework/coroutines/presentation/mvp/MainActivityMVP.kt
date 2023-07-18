package otus.homework.coroutines.presentation.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer

class MainActivityMVP : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsPresenter = CatsPresenter(diContainer.useCase)
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
