package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.di.DiContainer
import otus.homework.coroutines.di.MainActivityScreenComponent
import otus.homework.coroutines.di.MainActivityScreenComponentImpl
import otus.homework.coroutines.di.MainActivityScreenDependencies

class MainActivity : AppCompatActivity() {

    private val mainActivityScreenDependencies: MainActivityScreenDependencies = DiContainer()
    private val screenComponent: MainActivityScreenComponent =
        MainActivityScreenComponentImpl.create(mainActivityScreenDependencies)

    private val catsPresenter: CatsPresenter by lazy { screenComponent.providePresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.presenter = catsPresenter
        catsPresenter.attachView(view)
        catsPresenter.onInitComplete()

    }

    override fun onStop() {
        catsPresenter.onStop()

        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    override fun onDestroy() {
        catsPresenter.detachView()
        super.onDestroy()
    }
}