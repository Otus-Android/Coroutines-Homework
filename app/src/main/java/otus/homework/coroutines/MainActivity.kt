package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory =
            ViewModelFactory(diContainer.service, diContainer.photoService)
        val catsViewModel = ViewModelProvider(this, factory = viewModelFactory)[CatViewViewModel::class.java]

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.getViewById(R.id.button).setOnClickListener {
            catsViewModel.onInitComplete()
        }
        catsViewModel.catsLiveData.observe(this, Observer { result ->
            when (result) {
                is Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }

                is Success -> {
                    view.populate(result.fact)
                }
            }
        })
    }
}