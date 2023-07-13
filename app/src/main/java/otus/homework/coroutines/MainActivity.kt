package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter
//    private val diContainer = DiContainer()

    lateinit var vm: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        vm = ViewModelProvider(this, CatsViewModelFactory()).get(CatsViewModel::class.java)
        vm.stateLiveData.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    view.populate(result.value)
                }

                is Result.Error -> {
                    view.showError(result.throwable.toString())
                }
            }
        }

        view.setOnClickListener { vm.loadData() }

//        catsPresenter = CatsPresenter(diContainer.catService, diContainer.catImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}
