package otus.homework.coroutines.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.CatsView
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.R

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(DiContainer.catsRepo, DiContainer.presenterScope)
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