package otus.homework.coroutines.presentation.mvvm

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import otus.homework.coroutines.R
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.domain.CatModel

class MainActivityMVVM : AppCompatActivity() {

    private val diContainer = DiContainer()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = diContainer.mainViewModel
        initialize()
    }


    private fun initialize() {
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.loadCatModel()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is MainState.Success -> {
                            state.item?.let { populate(state.item) }
                        }
                        is MainState.Error -> {
                            Toast.makeText(
                                this@MainActivityMVVM,
                                R.string.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun populate(cat: CatModel) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact
        Picasso.get().load(cat.imageUrl).into(findViewById<ImageView>(R.id.imageView))
    }
}
