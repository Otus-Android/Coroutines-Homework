package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import otus.homework.coroutines.data.CatDTO
import otus.homework.coroutines.viewmodel.CatsViewModel
import otus.homework.coroutines.viewmodel.CatsViewModelFactory
import otus.homework.coroutines.viewmodel.Result

class MainActivity : AppCompatActivity() {

    //    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by viewModels {
        CatsViewModelFactory(
            diContainer.factService,
            diContainer.photoService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        val catFact = findViewById<TextView>(R.id.fact_textView)
        val catPhoto = findViewById<ImageView>(R.id.cat_photo)
        val refreshButton = findViewById<Button>(R.id.button)

        refreshButton.setOnClickListener {
            viewModel.onInitComplete()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.catData.collect { result ->
                    when (result) {
                        is Result.Success<CatDTO> -> {
                            catFact.text = result.value.fact.text
                            Picasso.get().load(result.value.photo.photoUrl).into(catPhoto)
                        }
                        is Result.Error -> {
                            Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
//        catsPresenter = CatsPresenter(diContainer.factService, diContainer.photoService)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
    }

//    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
//        super.onStop()
//    }
}