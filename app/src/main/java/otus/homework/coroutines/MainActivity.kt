package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.network.facts.CatFactServiceList
import otus.homework.coroutines.network.facts.ninja.NinjaDiContainer
import otus.homework.coroutines.network.facts.old.DiContainer

/**
It responses which "cat fact" service will be used
 **/
val currentFactService = CatFactServiceList.NINJA_FACTS

class MainActivity : AppCompatActivity() {
    private val diContainer = when (currentFactService) {
        CatFactServiceList.HEROKUAPP -> DiContainer()
        CatFactServiceList.NINJA_FACTS -> NinjaDiContainer()
    }
    private val mainViewModel = MainViewModel(diContainer.factService, diContainer.imageService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        val catsView = findViewById<CatsView>(R.id.catsView)
        catsView.setUpButtonCallback(mainViewModel::onStart)

        mainViewModel.attachView(view)
        mainViewModel.onStart()
    }
}