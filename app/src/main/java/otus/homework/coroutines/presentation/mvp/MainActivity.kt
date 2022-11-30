package otus.homework.coroutines.presentation.mvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catsFactService = diContainer.serviceFact,
            catsImgService = diContainer.serviceImg,
            presenterScope = diContainer.presenterScope,
            errorDisplay = diContainer.errorDisplay,
            managerResources = diContainer.managerResources
        )

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            catsPresenter.stopCoroutines()
        }
        super.onStop()
    }
}