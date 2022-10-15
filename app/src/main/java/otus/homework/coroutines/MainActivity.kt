package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.network.facts.CatFactServiceList
import otus.homework.coroutines.network.facts.ninja.NinjaDiContainer
import otus.homework.coroutines.network.facts.old.DiContainer

/**
It responses which "cat fact" service will be used
 **/
val currentFactService = CatFactServiceList.HEROKUAPP

class MainActivity : AppCompatActivity() {

    private lateinit var catsPresenter: CatsPresenter

    private val diContainer = when (currentFactService) {
        CatFactServiceList.HEROKUAPP -> DiContainer()
        CatFactServiceList.NINJA_FACTS -> NinjaDiContainer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.factService, diContainer.imageService)
        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        catsPresenter.stopCatJob()
        super.onStop()
    }
}