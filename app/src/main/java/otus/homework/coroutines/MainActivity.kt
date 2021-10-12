package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        catsPresenter = CatsPresenter(diContainer.serviceFact, diContainer.servicePic)
        binding.catView.presenter = catsPresenter
        catsPresenter.attachView(binding.catView)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}