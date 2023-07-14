package otus.homework.coroutines.presentation.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R
import otus.homework.coroutines.utils.CustomApplication

class ViewWithPresenterActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_view_with_presenter, null) as CatsView
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