package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    lateinit var catsPresenter: CatsPresenter
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.service
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsViewModel.getCatsFact()
        view.findViewById<Button>(R.id.button).setOnClickListener{
            catsViewModel.getCatsFact()
        }

        val catsObserver = Observer<CatsViewModel.Result> { result ->
            if (result is CatsViewModel.Result.Success) {
                view.populate(FactAndPicture(result.factAndPicture.fact, result.factAndPicture.picture))
            } else if (result is CatsViewModel.Result.Error){
                view.showError(result.message)
            }
        }

        catsViewModel.getObservableData().observe(this, catsObserver)
    }

}