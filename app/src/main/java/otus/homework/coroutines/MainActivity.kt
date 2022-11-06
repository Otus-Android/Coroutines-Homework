package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    //lateinit var catsPresenter: CatsPresenter
    //private val diContainer = DiContainer()
    private val viewModelFactory: CatsViewModelFactory = CatsViewModelFactory()
    private val catsVm: CatsViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsVm.fetchData()
        view.findViewById<Button>(R.id.button).setOnClickListener {
            catsVm.fetchData()
        }
        catsVm.livePresentationModel.observe(this) {
            when (it) {
                is Result.Success<*> -> view.populate(it.successBody as CatPresentationModel)
                is Result.Error -> view.showMessage(it.message)
            }
        }
        //catsPresenter = CatsPresenter(diContainer.service, this)
        //view.presenter = catsPresenter
        //catsPresenter.attachView(view)
        //catsPresenter.onInitComplete()


    }

    override fun onStop() {
        if (isFinishing) {
            //catsPresenter.detachView()
        }
        super.onStop()
    }

}