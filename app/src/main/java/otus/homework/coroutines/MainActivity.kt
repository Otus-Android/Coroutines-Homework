package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(
//            diContainer.catsService,
//            diContainer.catsImagesService
//        )
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        viewModel = ViewModelProvider(
            this,
            CatsViewModel.factory(diContainer.catsService, diContainer.catsImagesService)
        ).get(CatsViewModel::class.java)

        view.setOnClickRefreshButtonListener {
            viewModel.update()
        }

        viewModel.stateLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.value)
                }

                is Result.Error -> {
                    view.showShortToast(result.throwable.toString())
                }
            }
        }
    }

    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
        super.onStop()
    }
}
