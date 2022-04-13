package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val catsViewModel: CatsViewModel by viewModels {
        CatsViewModel.CatsViewModelFactory(diContainer.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null)
        setContentView(view)
        catsViewModel.imageData.observeForever {
            Picasso.get().load(it)
                .into(cat_image)
        }
        catsViewModel.factData.observeForever {
            findViewById<TextView>(R.id.fact_textView).text = it
        }
        catsViewModel.errorMessage.observeForever {
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT).show()
        }
        button?.setOnClickListener {
            catsViewModel.onInitComplete()
        }
        catsViewModel.onInitComplete()
    }
}