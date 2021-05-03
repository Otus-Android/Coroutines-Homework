package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()
    private lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        /*
            ### Реализовать решение ViewModel
         */
        viewModel = CatsViewModel(diContainer.serviceFact, diContainer.serviceImage)

        viewModel.getCatFactImage()

        val refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe)
        refreshLayout?.setOnRefreshListener {
            viewModel.getCatFactImage()
        }

        viewModel.catsResponse.observe(this, Observer { factImage ->
            refreshLayout.isRefreshing = false
            when (factImage) {
                is Result.Success -> {
                    findViewById<TextView>(R.id.fact_textView).text = factImage.value.fact?.text
                    Picasso.get().load(factImage.value.image?.file)
                        .into(findViewById<ImageView>(R.id.iv_image))
                }
                is Result.Error -> {
                    Toast.makeText(this, factImage.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        //++++++++++++++++++++++++++++
        /*
            ### Перейти с коллбеков на саспенд функции и корутины
            ### Добавить к запросу фактов запрос рандомных картинок с [https://aws.random.cat/meow](https://aws.random.cat/meow)
         */
//        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.serviceImage)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}