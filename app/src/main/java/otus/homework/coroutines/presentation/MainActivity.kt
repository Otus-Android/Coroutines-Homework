package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.di.factRepository
import otus.homework.coroutines.di.imageUrlRepository

class MainActivity : AppCompatActivity() {

    private val screenPresenter: CatsPresenter by lazy {
        CatsPresenter(factRepository, imageUrlRepository)
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.let { view ->
            view.presenter = screenPresenter
            screenPresenter.attachView(view)
            screenPresenter.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            screenPresenter.detachView()
        }
        super.onStop()
    }
}