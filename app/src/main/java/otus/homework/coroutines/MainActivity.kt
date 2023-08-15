package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    lateinit var viewModal: MainActivityViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModal = ViewModelProvider(this, CatsViewModelFactory(
            diContainer.serviceFact,
            diContainer.serviceImage
        ))[MainActivityViewModal::class.java]
        //Refresh date
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModal.loadData()
        }

        viewModal.state.observe(this){ result->
            when(result){
                is ResponseResult.Success -> view.populate(result.catModal)
                is ResponseResult.Error -> view.showToast(result.throwable.message.orEmpty())
            }

        }

//        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.serviceImage)
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