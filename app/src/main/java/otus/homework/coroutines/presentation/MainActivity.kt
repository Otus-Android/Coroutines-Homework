package otus.homework.coroutines.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.di.catsService

class MainActivity : AppCompatActivity() {


    private val catsPresenter: CatsPresenter by lazy {
        CatsPresenter(catsService)
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.let { view ->
            view.presenter = catsPresenter
            catsPresenter.attachView(view)
            catsPresenter.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}