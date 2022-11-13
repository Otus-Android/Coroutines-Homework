package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    val catsViewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service, diContainer.awsService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()


        view.catsViewModel = catsViewModel
        view.service = diContainer.service
        view.awsService = diContainer.awsService


        catsViewModel.onInitComplete(diContainer.service, diContainer.awsService)

        catsViewModel.catsFactData.observe(this) { result ->
            when(result) {
               is Result.Success ->  result.data?.let { it -> view.populate(it) }
               is Result.Error -> {
                   result.msg?.let { view.showToast(it) }
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