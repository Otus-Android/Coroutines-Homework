package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(this, diContainer.service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        runBlocking {
            launch {
                catsPresenter.onInitComplete()
            }
        }

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}