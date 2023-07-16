package otus.homework.coroutines.presentation.mvp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R
import otus.homework.coroutines.utils.CustomApplication

class CatsActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    @SuppressLint("InflateParams")
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