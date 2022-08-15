package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutInflater.inflate(R.layout.activity_main, null))

        val textView = findViewById<TextView>(R.id.fact_textView)
        val imageView = findViewById<ImageView>(R.id.imageView)
        findViewById<Button>(R.id.button).apply { this.setOnClickListener { catsViewModel.onButtonClicked() } }

        catsViewModel = CatsViewModel(diContainer.service, diContainer.imageService)
        catsViewModel.getCatInfo().observe(this){
            when(it){
                is Error ->{
                    textView.text = it.errorMsg
                    Picasso.get()
                        .load(R.drawable.ic_launcher_foreground)
                        .fit()
                        .centerInside()
                        .into(imageView)
                }
                is Success<*> ->{
                    if (it.data is CatsModel){
                        textView.text = it.data.fact.text
                        Picasso.get()
                            .load(it.data.image.fileUrl)
                            .fit()
                            .centerInside()
                            .into(imageView)
                    }

                }
            }
        }
    }

}