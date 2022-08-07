package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  private val diContainer = DiContainer()
  private val catsViewModel: CatsViewModel by viewModels {
    CatsViewModelFactory(diContainer.catFactService, diContainer.catImageService)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
    setContentView(view)

    findViewById<Button>(R.id.button).setOnClickListener {
      catsViewModel.getCatData()
    }

    catsViewModel.catLiveData.observe(this) { result ->
      when (result) {
        is Success -> view.populate(result.value)
        is Error -> view.showToast(result.message)
      }
    }
  }
}