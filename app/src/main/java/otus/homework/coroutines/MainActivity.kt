package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private lateinit var viewModel: CatViewModel

    private val diContainer = DiContainer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service)
        viewModel= ViewModelProvider(this, CatViewModelFactory(diContainer.service))
            .get(CatViewModel::class.java)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
       // catsPresenter.onInitComplete()

        viewModel.gen.observe(this){
            view.populate(it.fact,it.img)
        }
        viewModel.error.observe(this){
            view.showError(it)
        }
    }
    fun getCats(){
        viewModel.getCat()
    }
    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}