package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModel.Factory(diContainer.textService, diContainer.imageService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.textService, diContainer.imageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        catsViewModel.onInitComplete()
        view.viewModel = catsViewModel
        catsViewModel.catsData.observe(this) { result ->
            when (result) {
                is Result.Success -> view.populate(result.value)
                is Result.Failure -> view.showErrorMessage(result.message ?: "Ошибка соединения")
            }
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            with (catsPresenter) {
                detachView()
                cancelJob()
            }
        }
        super.onStop()
    }
}