package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

//    private lateinit var catsPresenter: CatsPresenter
    private var catsViewModelFactory = CatsViewModelFactory(this)
    private lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter =
//            CatsPresenter(this, diContainer.catsFactService, diContainer.catsImageService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        catsViewModel = ViewModelProvider(this, catsViewModelFactory).get(CatsViewModel::class.java)
        catsViewModel.catDataLiveData.observe(this) { data ->
            data?.let { view.populate(it) }
        }
        catsViewModel.loadCatData()
        view.findViewById<Button>(R.id.button).setOnClickListener {
            catsViewModel.loadCatData()
        }
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}