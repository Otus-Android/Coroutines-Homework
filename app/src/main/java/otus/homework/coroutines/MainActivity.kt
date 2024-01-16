package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val scope = PresenterScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.serviceFact,diContainer.serviceImage)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        scope.launch {
            catsPresenter.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            scope.cancel("Stop PresenterScope in Activity")
            catsPresenter.detachView()
        }
        super.onStop()
    }
}