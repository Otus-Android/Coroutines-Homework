package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel by viewModels<CatViewModel> { CatViewModelFactory(diContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.data.observe(this) {
            when (it) {
                is Result.Success -> {
                    findViewById<TextView>(R.id.fact_textView).text = it.value.fact.text
                    Picasso.get()
                        .load(it.value.image.url)
                        .into(findViewById<ImageView>(R.id.image))
                }
                is Result.Error -> Toast
                    .makeText(baseContext, it.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener { viewModel.refresh() }
    }
}