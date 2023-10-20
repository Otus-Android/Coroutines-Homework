package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import otus.homework.coroutines.viewModel.CatsEvent
import otus.homework.coroutines.viewModel.CatsState

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private lateinit var view: CatsView

    private val viewModel by lazy { diContainer.catsViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.setClickListener {
            viewModel.onInitComplete()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    renderState(state)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.events
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { event ->
            handleEvent(event)
        }.launchIn(lifecycleScope)
    }

    private fun renderState(state: CatsState) {
        when(state) {
            is CatsState.Data -> {
                view.populate(state.meowInfo)
            }
            is CatsState.Error -> {}
            is CatsState.Init -> {}
        }
    }
    private fun handleEvent(event: CatsEvent) {
        when (event) {
            is CatsEvent.Error -> {
                view.showToast(event.msg)
            }
        }
    }
}