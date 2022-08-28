package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private var catsView: CatsView? = null

    private val viewModel: CatsViewModel by lazy {
        CatsViewModelFactory(
            application,
            diContainer.service,
            resources
        ).create(CatsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catsView = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        catsView?.let { view ->
            setContentView(view)

            view.viewModel = viewModel
            viewModel.attachView(view)
            viewModel.onInitComplete()
        }
    }

    override fun onStop() {
        if (isFinishing) {
            viewModel.detachView()
            catsView?.stopPictureLoading()
        }
        super.onStop()
    }
}