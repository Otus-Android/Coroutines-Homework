package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private var catsView: CatsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        catsView?.let { view ->
            setContentView(view)

            catsPresenter = CatsPresenter(diContainer.service, resources)
            view.presenter = catsPresenter
            catsPresenter.attachView(view)
            catsPresenter.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
            catsView?.stopPictureLoading()
        }
        super.onStop()
    }
}