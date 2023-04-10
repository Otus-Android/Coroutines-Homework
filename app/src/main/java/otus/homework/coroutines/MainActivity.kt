package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by viewModels {
        CatsViewModel.factory(diContainer.catsRepository)
    }

    private lateinit var catsView: CatsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)

        catsView.setOnButtonClick(viewModel::updateFact)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvents.collect(::handleEvent)
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::renderView)
            }
        }
    }

    private fun handleEvent(event: CatsViewModel.UiEvent) {
        when (event) {
            is CatsViewModel.UiEvent.ShowError -> {
                catsView.showError(event.message)
            }
        }
    }

    private fun renderView(state: CatsViewModel.UiState) {
        when (state) {
            is CatsViewModel.UiState.Success -> {
                catsView.populate(state.data)
            }

            is CatsViewModel.UiState.Idle -> {
                // Do nothing
            }
        }
    }
}