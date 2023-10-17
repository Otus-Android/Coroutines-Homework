package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private lateinit var viewModel: MainViewModel
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.catsImageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        val factory = MainViewModelFactory(diContainer.catsService, diContainer.catsImageService)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        viewModel.result.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.data)
                }
                is Result.Error -> {
                    view.showToast(result.errorMessage)
                }
            }
        }
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInit()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}