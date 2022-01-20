package otus.homework.coroutines.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.DiContainer
import otus.homework.coroutines.controller.CatsPresenter
import otus.homework.coroutines.databinding.ActivityMainBinding
import otus.homework.coroutines.viewmodel.CatsViewModel

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val di by lazy { DiContainer() }
    private lateinit var catsPresenter: CatsPresenter
    private lateinit var model: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initModel()
        initPresenter()
    }

    private fun initModel() {
        model = CatsViewModel(di.factsService, di.picsService)
        model.state.observe(this) { catsPresenter.onStateChanged(it) }
    }

    private fun initPresenter() {
        catsPresenter = CatsPresenter(binding.catsInfoView, model)
        binding.catsInfoView.presenter = catsPresenter
    }

}
