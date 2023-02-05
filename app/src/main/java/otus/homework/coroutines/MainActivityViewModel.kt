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

class MainActivityViewModel : AppCompatActivity() {

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            owner = this,
            factory = CatsViewModel.CatModelFactory(
                diContainer.catsFactService, diContainer.catsImageService
            ),
        )[CatsViewModel::class.java]
        viewModel.onInitComplete()

        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInitComplete()
        }


        lifecycleScope.launch {
            viewModel.catInfo.observe(this@MainActivityViewModel) { result ->
                when (result) {
                    is Result.Success -> {
                        findViewById<TextView>(R.id.fact_textView).text = result.value.text
                        Picasso.get().load(result.value.imageUrl)
                            .into(findViewById<ImageView>(R.id.cat_image_view));
                    }
                    is Result.Error -> {
                        Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }
}