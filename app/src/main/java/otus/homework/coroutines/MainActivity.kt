package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.di.MainActivityScreenComponent
import otus.homework.coroutines.di.MainActivityScreenComponentImpl
import otus.homework.coroutines.di.MainActivityScreenDependencies

class MainActivity : AppCompatActivity() {

    private val mainActivityScreenDependencies: MainActivityScreenDependencies = DiContainer()
    private val screenComponent: MainActivityScreenComponent =
        MainActivityScreenComponentImpl.create(mainActivityScreenDependencies)

    private val factoryProducer: ViewModelProvider.Factory by lazy {
        screenComponent.provideViewModelFactory()
    }

    private val viewModel: CatsViewModel by viewModels { factoryProducer }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = viewModel
        viewModel.onInitComplete()

        viewModel.fact.observe(this) { result ->
            view.render(result)
        }
    }

    override fun onStop() {
        viewModel.onStop()

        super.onStop()
    }
}