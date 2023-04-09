package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.factService, diContainer.picsService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//
//        catsPresenter.onInitComplete()
//
        val model = CatsViewModel(diContainer.factService, diContainer.picsService)

        findViewById<Button>(R.id.button).setOnClickListener {
            model.getData()
        }

        model.catFact.observeForever {

            if (it is Result.Success<CatFact>) {
                val fact = (it as? Result.Success<CatFact>)?.data
                fact?.let { catFact ->
                    findViewById<TextView>(R.id.fact_textView).text = catFact.factText
                    Picasso.get().load(catFact.imageUrl).into(findViewById<ImageView>(R.id.cat_pic))
                }
            } else {
                Toast.makeText(this, (it as? Result.Error)?.errorMassage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStop() {
        if (isFinishing) {
//            catsPresenter.detachView()
        }
        super.onStop()
    }
}