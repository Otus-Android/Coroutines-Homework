package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter

    lateinit var mViewModel: CatsViewModel

    private val diContainer = DiContainer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        catsPresenter = CatsPresenter(diContainer.service, diContainer.imageService, applicationContext)
        mViewModel = ViewModelProvider(
            this,
            CatsViewModelFactory(diContainer.service, diContainer.imageService)
        ).get(CatsViewModel::class.java)
        view.catsViewModel = mViewModel


        mViewModel.viewState.observe(this, Observer {
            when (it) {
                is Success<*> -> {
                    view.populate(it.data as Data)
                }
                is Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    CrashMonitor.trackWarning()
                }
            }
        })
    }

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }
}