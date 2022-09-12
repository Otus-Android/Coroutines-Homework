package otus.homework.coroutines

import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

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
            presenterScope = PresenterScope()
        )
        view.presenter = catsPresenter
        catsPresenter.attachView(view)

//        catsPresenter.onInitComplete() //uncomment to use CatsPresenter
        viewModelSetUp() //uncomment to use CatsViewModel
    }

    private fun viewModelSetUp() {
        catsViewModel = CatsViewModel(
            catsService = diContainerFact.service,
            catsImageService = diContainerImage.service
        )

        catsViewModel.refreshContent()

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            catsViewModel.refreshContent()
        }
        val imageView = findViewById<ImageView>(R.id.image)
        val textView = findViewById<TextView>(R.id.fact_textView)

        catsViewModel.event.observe(this) { event ->
            event.transferIfNotHandled()?.let { content ->
                when(content) {
                    is String -> Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
                    is CatsContent -> {
                        content.fact?.let { textView.text = it.text }
                        content.imageUrl?.let { Picasso.get().load(it.fileUrl).into(imageView) }
                    }
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