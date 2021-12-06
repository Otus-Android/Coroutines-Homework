package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.model.CatModel

class MainActivity : AppCompatActivity() {

   // lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewModel
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        //catsPresenter = CatsPresenter(diContainer.service)
        catsViewModel = CatsViewModel(diContainer.service)
        /*view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()*/
        catsViewModel.catsData.observe(this) { result ->
            when (result) {
                is Success<*> ->
                    view.populate(result.data as CatModel)
                is Error ->
                    view.toast(result.message)
            }
        }

        view.clickAction = catsViewModel::loadData
        catsViewModel.loadData()
    }

    override fun onStop() {
        if (isFinishing) {
            //catsPresenter.detachView()
            catsViewModel.cancel()
        }
        super.onStop()
    }
}