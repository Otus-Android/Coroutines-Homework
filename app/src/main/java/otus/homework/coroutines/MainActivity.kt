package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewModel

    private val diContainerFact = DiContainer(
        baseUrl = "https://cat-fact.herokuapp.com/facts/",
        serviceClass = CatsService::class.java
    )

    private val diContainerImage = DiContainer(
        baseUrl = "https://aws.random.cat/",
        serviceClass = CatsImageService::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(
            catsService = diContainerFact.service,
            catsImageService = diContainerImage.service,
            presenterScope = PresenterScope(),
            dispatcherIo = Dispatchers.IO
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)

//        catsPresenter.onInitComplete() //uncomment to use CatsPresenter
        viewModelSetUp() //uncomment to use CatsViewModel
    }

    private fun viewModelSetUp() {
        catsViewModel = CatsViewModel(
            catsService = diContainerFact.service,
            catsImageService = diContainerImage.service,
            dispatcherIo = Dispatchers.IO
        )

        catsViewModel.refreshContent()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            catsViewModel.refreshContent()
        }
        val imageView = findViewById<ImageView>(R.id.image)
        val textView = findViewById<TextView>(R.id.fact_textView)

        catsViewModel.content.observe(this) { event ->
            event.transferIfNotHandled()?.let { content ->
                content.fact?.let { textView.text = it.text }
                content.imageUrl?.let { Picasso.get().load(it.fileUrl).into(imageView) }
            }
        }
        catsViewModel.showToast.observe(this) { event ->
            event.transferIfNotHandled()?.let { text ->
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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