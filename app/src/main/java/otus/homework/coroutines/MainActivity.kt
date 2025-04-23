package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import otus.homework.coroutines.Result.*

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private lateinit var fact: TextView
    private lateinit var image: ImageView
    private lateinit var loadButton: Button

    private val viewModel by lazy {
        ViewModelProvider(
            this, CatsViewModel.provideFactory(diContainer.catsService, diContainer.imageService)
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        fact = view.findViewById(R.id.fact_textView)
        image = view.findViewById(R.id.catImage)
        loadButton = view.findViewById(R.id.button)

        processState()
        setListeners()
    }

    private fun setListeners() {
        loadButton.setOnClickListener {
            viewModel.load()
        }
    }

    private fun processState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state) {
                    is Success<*> -> {
                        (state.data as CatsUIState).also {
                            fact.text = it.fact.fact
                            Picasso.get().load(it.image.url).into(image)
                        }
                    }
                    is Error -> {
                        showToast(state.error)
                        viewModel.load()
                    }
                }
            }
        }
    }

    private fun showToast(throwable: Throwable, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(this, throwable.message, duration).show()
    }
}