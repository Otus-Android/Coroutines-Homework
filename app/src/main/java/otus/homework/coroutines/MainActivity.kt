package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.model.entity.Result
import otus.homework.coroutines.view.CatsView
import otus.homework.coroutines.viewmodel.CatsViewModel

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel: CatsViewModel by lazy {
        ViewModelProvider(this, CatsViewModel.Factory(diContainer.catsUseCase)).get()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView

        view.onMoreButtonCallback(catsViewModel::onCatFactRequestAction)
        setContentView(view)

        lifecycleScope.launch { observeViewModel(view) }
        catsViewModel.onCatFactRequestAction()
    }

    private suspend fun observeViewModel(view: CatsView) {
        catsViewModel.factResultFlow.collect {
            when (it) {
                is Result.Success -> view.populate(it.item)
                is Result.Error -> showToast(R.string.socket_timeout_error)
            }
        }
    }

    private fun showToast(
        @StringRes message: Int
    ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}