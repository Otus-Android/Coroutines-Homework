package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    val catsViewModel: CatsViewModel by viewModels()

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.serviceImage)

        catsViewModel.catsServiceFact = diContainer.serviceFact
        catsViewModel.catsServiceImg = diContainer.serviceImage

        catsViewModel.requestData()
        view.findViewById<Button>(R.id.button).setOnClickListener{
            catsViewModel.requestData()
        }

        val catsObserver = Observer<Result> { result ->
            // Update the UI, in this case, a TextView.
            if (result is Result.Success) {
                view.populate(IllustratedFact(result.data.image, result.data.fact))
            } else if (result is Result.Error){
                view.showErrorText(result.messageText)


            }

        }

        catsViewModel.getObservableData.observe(this, catsObserver)

//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}