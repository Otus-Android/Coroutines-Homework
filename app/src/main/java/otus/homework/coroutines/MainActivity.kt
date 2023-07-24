package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mainViewModel by lazy { MainViewModel(
        diContainer.factService,
        diContainer.imageService
    ) }

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state
                    .collect { result -> view.populate(result) }
            }
        }
        view.refreshFun = object : Refresh {
            override fun call() {
                mainViewModel.refresh()
            }
        }
        mainViewModel.refresh()

        /* For test CatsPresenter
        CatsPresenter(diContainer.factService,
            diContainer.imageService,
            applicationContext,
            diContainer.mainDispatcher)
            .onInitComplete() // */
    }
}