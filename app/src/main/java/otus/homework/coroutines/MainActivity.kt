package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.cancel
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.presenter.CatsPresenter
import otus.homework.coroutines.view.CatsView

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.catsUseCase, diContainer.presenterScope)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete(applicationContext)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            diContainer.presenterScope.cancel()
        }
        super.onStop()
    }
}