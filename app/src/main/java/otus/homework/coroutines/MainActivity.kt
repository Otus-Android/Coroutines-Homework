package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CatsViewModel>()

    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var imgView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.button)
        textView = findViewById(R.id.fact_textView)
        imgView = findViewById(R.id.random_imgView)

        viewModel.onInitComplete()

        button.setOnClickListener {
            viewModel.onInitComplete()
        }

        viewModel.factFlow.onEach {
            when (it) {
                is Result.Error -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                is Result.Success -> {
                    textView.text = it.data.fact

                    Picasso.get()
                        .load(it.data.imgLink)
                        .into(imgView)
                }
            }
        }.observeInLifecycle(this)
    }
}
