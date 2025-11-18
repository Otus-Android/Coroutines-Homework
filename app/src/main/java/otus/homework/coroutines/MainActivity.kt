package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.presentation.CatsPresenter
import otus.homework.coroutines.presentation.CatsView
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catsFactService = diContainer.catsFactService,
            catsImageService = diContainer.catsImageService,
            context = WeakReference(this)
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        // Вариант с viewmodel, не использовал ktx (решил вспомнить как все ручками делать))
//        val viewModel: CatsViewModel = ViewModelProvider(
//            owner = this,
//            factory = CatsViewModel.provideFactory(
//                catsFactService = diContainer.catsFactService,
//                catsImageService = diContainer.catsImageService,
//                this,
//            )
//        )[CatsViewModel::class.java]
//        viewModel.onInitComplete()
//
//        view.onButtonClick {
//            viewModel.catsJobCancel()
//            viewModel.onInitComplete()
//        }
//        viewModel.uiState.observe(this) { result ->
//            when (result) {
//                is Result.Loading -> {}
//                is Result.Error -> {}
//                else -> {
//                    val model = result as? Result.Success<CatsInfo>
//                    model?.data?.let { view.populate(model = it) }
//                }
//            }
//        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    override fun onDestroy() {
        catsPresenter.catsJobCancel()
        super.onDestroy()
    }
}
