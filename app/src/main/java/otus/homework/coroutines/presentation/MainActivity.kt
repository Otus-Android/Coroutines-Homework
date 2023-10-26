package otus.homework.coroutines.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.domain.DiContainer
import otus.homework.coroutines.models.presentation.CatInfoModel
import otus.homework.coroutines.models.presentation.Text

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.Factory
        )[MainViewModel::class.java]
    }
    private lateinit var view: CatsView
    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private val diContainer = DiContainer()

    private val shouldUseViewModel = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        if (shouldUseViewModel) {
            initViews()
            observeOnData()
            viewModel.onInit()
        } else {
            initPresenter()
        }
    }

    override fun onStop() {
        if (isFinishing && !shouldUseViewModel) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun initPresenter() {
        initPresenter()
        catsPresenter = CatsPresenter(diContainer.catsService, diContainer.catsIconService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onClick()
    }

    private fun initViews() {
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.onInit()
        }
        textView = findViewById(R.id.fact_textView)
        imageView = findViewById(R.id.image_imageView)
    }

    private fun observeOnData() {
        viewModel.catResultLiveData.observe(this, ::onCatResult)
    }

    private fun onCatResult(result: CatResult) {
        when (result) {
            is CatResult.Success -> onCatResultSuccess(result.catInfo)
            is CatResult.Error -> onToastTextChanged(result.toastText)
        }
    }

    private fun onCatResultSuccess(model: CatInfoModel) {
        textView.text = model.text
        Picasso.get()
            .load(model.iconUrl)
            .resize(model.width, model.height)
            .centerCrop()
            .into(imageView)
    }

    private fun onToastTextChanged(text: Text) {
        Toast.makeText(this, text.getString(this), Toast.LENGTH_SHORT).show()
    }
}