package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private val viewModel: CatsViewModel by viewModels(factoryProducer = { CatsViewModel.Factory(diContainer.catsService, diContainer.catsPictureService) })

//    lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener {
           viewModel.load()
        }
        val text = findViewById<TextView>(R.id.fact_textView)
        val image = findViewById<ImageView>(R.id.fact_imageView)

        viewModel.load()
        viewModel.liveData.observe(this) {
            when (it) {
                is CatsViewModel.Result.Success<Cat> -> {
                    val value = it.result
                    text.text = value.fact.text
                    Picasso.get().load(value.picture).into(image)
                }
                is CatsViewModel.Result.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

//        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.catsPictureService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
        super.onStop()
    }
}