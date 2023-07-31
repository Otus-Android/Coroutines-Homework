package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import otus.homework.coroutines.Result

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    private val viewModel by viewModels<AnimalViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service, PicturesContainer().service)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

        findViewById<Button>(R.id.button_vm).setOnClickListener {
            viewModel.getData()
        }

        viewModel._liveData.observe(this) {

            val txt = findViewById<TextView>(R.id.fact_textView)
            val img = findViewById<ImageView>(R.id.imageView)

            when (viewModel.getState()) {
                Result.Loading -> {
                    txt.text = "Loading..."
                    img.isVisible = false
                }
                Result.Error -> {
                    txt.text = "Error"
                    img.isVisible = false
                }
                Result.Success -> {
                    txt.text = it.fact
                    Picasso.get().load(it.image).into(img)
                    img.isVisible = true
                }
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}
