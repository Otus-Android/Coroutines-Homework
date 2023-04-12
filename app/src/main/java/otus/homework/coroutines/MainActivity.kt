package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        mainViewModel = ViewModelProvider(this,
            MainViewModelFactory (diContainer.serviceFact, diContainer.servicePic)
        ).get(MainViewModel::class.java)

        view.onInflate(mainViewModel)

        mainViewModel.result.observe(this) { result ->
            when (result) {
                is Success -> view.populate(result.catData)
                is Error -> {
                    if(result.isSocketTimeoutException) view.showToast(result.errorMassage)
                }
            }
        }
    }
}