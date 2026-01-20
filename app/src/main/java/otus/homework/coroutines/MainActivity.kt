package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private lateinit var catsViewModel: CatsViewModel
    private lateinit var catsView: CatsView
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(catsView)


        catsViewModel = ViewModelProvider(
            this,

            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CatsViewModel(
                        diContainer.service,
                        diContainer.imageService
                    ) as T
                }
            }
        ).get(CatsViewModel::class.java)


        catsViewModel.result.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // прогресс
                }
                is Result.Success -> {
                    catsView.populate(result.data)
                }
                is Result.Error -> {
                    catsView.showToast(result.message)
                }
            }
        }

        catsViewModel.loadCatData()
    }


    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            // Очистка при необходимости
        }
    }
}