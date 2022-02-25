package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import otus.homework.coroutines.other.Resource

class MainActivity : AppCompatActivity() {
    /** Закомментировал логику презентера, так как использую ViewModel*/
//    lateinit var catsPresenter: CatsPresenter

    companion object {
        private const val TAG = "MainActivity"
    }

    private val diContainer = DiContainer()

    private val factTextView: TextView by lazy { findViewById(R.id.fact_textView) }
    private val imageView: ImageView by lazy { findViewById(R.id.cat_imageView) }
    private val factButton: Button by lazy { findViewById(R.id.button) }

    private val viewModel: CatsViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(diContainer.service)
        )[CatsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

//        catsPresenter = CatsPresenter(diContainer.service)
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()

        initObservers()
        initListeners()
    }


    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.result.collect { resource ->
                when (resource) {
                    is Resource.Error -> Toast.makeText(
                        applicationContext, resource.message,
                        Toast.LENGTH_LONG
                    ).show()
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        factTextView.text = resource.data?.text
                    }
                }
            }
            viewModel.imageUrl.collect { resource ->
                when (resource) {
                    is Resource.Error -> Toast.makeText(
                        applicationContext, resource.message,
                        Toast.LENGTH_LONG
                    ).show()
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        Picasso.get().load(resource.data?.file).fit().into(imageView)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        factButton.setOnClickListener {
            viewModel.onButtonNewFactPressed()
        }
    }


    override fun onStop() {
//        if (isFinishing) {
//            catsPresenter.detachView()
//        }
        super.onStop()
    }
}