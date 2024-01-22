package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

  private lateinit var catsPresenter: CatsPresenter

  private val diContainer = DiContainer()

  private lateinit var viewModel: MainViewModel

  private lateinit var factTextView: TextView
  private lateinit var avatarImageView: ImageView
  private lateinit var updateInfoButton: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
    setContentView(view)

    viewModel = ViewModelProvider(this)[MainViewModel::class.java]

    catsPresenter = CatsPresenter(diContainer.service, diContainer.image)
    view.presenter = catsPresenter
    //        catsPresenter.attachView(view)
    //        catsPresenter.onInitComplete()

    factTextView = findViewById(R.id.fact_textView)
    avatarImageView = findViewById(R.id.avatar_imageView)
    updateInfoButton = findViewById(R.id.button)
    observeLiveData()

    updateInfoButton.setOnClickListener { viewModel.onInitComplete() }
  }

  private fun observeLiveData() {
    viewModel.catInfoState.observe(this) { result ->
      when (result) {
        is Result.Success -> {
          setupCatInfo(result.catInfo)
        }
        is Result.Error -> {
          Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
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

  private fun setupCatInfo(catInfo: CatInfo) {
    Picasso.get()
        .load(catInfo.images.first().url)
        .resize(catInfo.images.first().width, catInfo.images.first().height)
        .into(avatarImageView)

    factTextView.text = catInfo.fact
  }
}
