package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.ui.CatsPresenter
import otus.homework.coroutines.ui.CatsView
import otus.homework.coroutines.util.DiContainer

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catsService = diContainer.catsService,
            picsService = diContainer.picsService
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            catsPresenter.cancelJob()
        }
        super.onStop()
    }
}