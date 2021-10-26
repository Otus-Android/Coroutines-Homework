package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MVP = false
    }

    lateinit var catsPresenter: CatsPresenter
    lateinit var catsViewModel: CatsViewModel

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        if(MVP) {
            catsPresenter = CatsPresenter(diContainer.service, diContainer.serviceImg)
            view.presenter = catsPresenter

            catsPresenter.attachView(view)
            catsPresenter.onInitComplete()

        } else {
            catsViewModel = ViewModelProvider(this, CatsViewModelFactory(diContainer.service, diContainer.serviceImg)).get(CatsViewModel::class.java)
            view.catsViewModel = catsViewModel

            catsViewModel.onInitComplete()
            catsViewModel.catsData.observe(this, {
                view.onDataChange(it)
            })
        }
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}