package otus.homework.coroutines.presenter.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import otus.homework.coroutines.CatsView
import otus.homework.coroutines.utils.DiContainer
import otus.homework.coroutines.R
import otus.homework.coroutines.data.Fact
import otus.homework.coroutines.data.ImgResource
import otus.homework.coroutines.data.Result
import otus.homework.coroutines.presenter.CatsPresenter
import otus.homework.coroutines.presenter.viewmodel.MainViewModel
import otus.homework.coroutines.presenter.viewmodel.MainViewModelFactory

class MainActivityViewModel : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    private val diContainer = DiContainer()
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(diContainer.service))[MainViewModel::class.java]
        val view = layoutInflater.inflate(R.layout.activity_main_viewmodel, null)
        setContentView(view)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        setListeners()


        mainViewModel.catData.observe(this){ result ->
            when(result) {
                is Result.Loading -> showLoading()
                is Result.Success -> {
                    populate(result.data.fact, result.data.imageUrl)
                    hideLoading()
                }
                is Result.Error -> {
                    hideLoading()
                    showToast(result.exception.message.toString())
                }
            }
        }

    }
    fun setListeners() {
        findViewById<Button>(R.id.button_refresh).setOnClickListener {
            mainViewModel.loadData()
        }
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            hideLoading()
            mainViewModel.cancelLoad()
        }
    }

    fun populate(factText: String, imageResourceUrl: String) {
        findViewById<TextView>(R.id.fact_textView).text = factText
        Picasso.get().load(imageResourceUrl).into(findViewById<ImageView>(R.id.fact_img))
    }

    fun showLoading() {
        progressBar.visibility = View.VISIBLE;
    }

    fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}