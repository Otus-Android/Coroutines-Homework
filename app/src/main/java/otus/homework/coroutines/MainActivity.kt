package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel = ViewModelProvider(this)[CatsViewModel::class.java]

        val diContainerFact = DiContainer("https://cat-fact.herokuapp.com/facts/")
        val diContainerImage = DiContainer("https://aws.random.cat/")

        findViewById<Button>(R.id.button).setOnClickListener{
            viewModel.onInitComplete()
        }

        catsPresenter = CatsPresenter(
            catsService = diContainerFact.factService,
            imagesService = diContainerImage.imageService,
        )

        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
        viewModel.attachView(view)
        viewModel.onInitComplete()


    }

    override fun onStop() {
        if (isFinishing) {
//            catsPresenter.detachView()
            viewModel.detachView()
        }
        super.onStop()
    }
}
