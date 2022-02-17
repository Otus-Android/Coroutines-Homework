package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter
//    private val diContainer = DiContainer()

    private val catsPresenterModel: CatsViewModel by lazy { ViewModelProvider(this)[CatsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(this, diContainer.factsService, diContainer.imagessService)
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
//        view.presenter = catsPresenter

        view.presenter = catsPresenterModel
        catsPresenterModel.attachView(view)
        catsPresenterModel.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenterModel.detachView()
        }
        catsPresenterModel.stop()
        super.onStop()
    }
}