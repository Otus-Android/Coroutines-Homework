package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

//    private lateinit var catsPresenter: CatsPresenter
    private lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter =
//            CatsPresenter(this, diContainer.catsFactService, diContainer.catsImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        catsViewModel =
            CatsViewModel(this, diContainer.catsFactService, diContainer.catsImageService)
        catsViewModel.loadCatData().observe(this) { data ->
            view.populate(data)
        }
        view.findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.loadCatData().observe(this) { data ->
                view.populate(data)
            }
        }
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}