package otus.homework.coroutines.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.controller.CatsPresenter
import otus.homework.coroutines.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var catsPresenter: CatsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initPresenter()
    }

    private fun initPresenter() {
        catsPresenter = CatsPresenter(DiContainer().factsService, DiContainer().picsService)
        binding.catsInfoView.presenter = catsPresenter
        catsPresenter.attachView(binding.catsInfoView)
        catsPresenter.updateData()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}
