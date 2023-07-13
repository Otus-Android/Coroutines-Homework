package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.utils.CustomApplication

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = createCatsPresenter()
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    private fun createCatsPresenter() = with(CustomApplication.diContainer(this)) {
        CatsPresenter(catRepository, stringProvider, dispatcher)
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}