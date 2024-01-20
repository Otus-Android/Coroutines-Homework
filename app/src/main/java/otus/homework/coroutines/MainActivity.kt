package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter


    private val diContainer = DiContainer()

    // private val scope = PresenterScope()

    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(
            diContainer.serviceFact,
            diContainer.serviceImage
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        //catsPresenter = CatsPresenter(diContainer.serviceFact,diContainer.serviceImage,scope)
        //view.presenter = catsPresenter
        view.viewModel = catsViewModel
        catsViewModel.attachView(view)
        catsViewModel.onInitComplete()

        /* CatsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
        */
    }

    override fun onStop() {
        if (isFinishing) {
            // scope.cancel("Stop PresenterScope in Activity")
            //catsPresenter.detachView()
            catsViewModel.detachView()
        }
        super.onStop()
    }
}