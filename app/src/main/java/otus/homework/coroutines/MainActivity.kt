package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = ViewModelProvider(this)[CatsViewModel::class.java]

        val diContainer = DiContainer()

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }

        catsPresenter = CatsPresenter(
            catsService = diContainer.factService,
            imagesService = diContainer.imageService
        )

        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        viewModel.onInitComplete()

        viewModel.result.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.content)
                }
                is Result.Error -> {
                    view.showToast(result.throwable.message.orEmpty())
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
//            catsPresenter.detachView()
        }
        super.onStop()
    }
}
