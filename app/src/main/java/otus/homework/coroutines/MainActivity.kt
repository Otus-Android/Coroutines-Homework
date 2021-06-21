package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.onEach
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.utils.launchWhenStarted

class MainActivity : AppCompatActivity() {

    //    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> { CatsViewModelFactory(diContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        view.initialize(ActivityMainBinding.bind(view), catsViewModel)
        catsViewModel.catsFactsFlow.onEach { new ->
            new?.let { view.populate(it) }
        }.launchWhenStarted(lifecycleScope)
        catsViewModel.errorMessagesFlow.onEach { error ->
            error?.let { view.showMessage(it) }
        }.launchWhenStarted(lifecycleScope)

        /*
        catsPresenter = CatsPresenter(diContainer.service)
        view.initialize(ActivityMainBinding.bind(view), catsPresenter)
        catsPresenter.apply {
            attachView(view)
            onInitComplete()
        }*/
    }

//    override fun onStop() {
//        if (isFinishing) {
////            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}