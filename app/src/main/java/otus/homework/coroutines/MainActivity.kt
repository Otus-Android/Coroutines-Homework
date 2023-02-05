package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel: CatViewModel by viewModels { CatViewModelFactory(diContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        clickButton()
        setupObserve()
    }

    private fun setupObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.catUiState.collect(::updateStateUi)
            }
        }

    }

    private fun updateStateUi(stateUi: CatUiState) {
        when (stateUi) {
            is CatUiState.Success -> {
                updateCat(stateUi.data)
            }
            is CatUiState.Error -> {
                CrashMonitor.trackWarning(stateUi.error)
            }
            else -> {}
        }
    }

    private fun updateCat(model: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        val imageView = findViewById<ImageView>(R.id.picture)
        Picasso.get().load(model.picture).into(imageView)

    }

    private fun clickButton() {
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getCat()
        }
    }
}