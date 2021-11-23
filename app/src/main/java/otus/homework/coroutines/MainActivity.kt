package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val fetchFactAndImageUseCase = FetchFactAndImageUseCase(
            catsService = diContainer.factService,
            randomImageService = diContainer.randomImageService
        )
        catsPresenter = CatsPresenter(fetchFactAndImageUseCase)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.onStop()
            catsPresenter.detachView()
        }
        super.onStop()
    }
}