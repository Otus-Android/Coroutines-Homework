package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val diContainer = DiContainer()
    private val viewModel: CatsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CatsViewModel.CatsViewModelFactory(diContainer.service)
        ).get(CatsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.resultLiveData.observe(this) {
            when (it) {
                is Error -> view.makeToast(it.error)
                is Success<*> -> when (it.result) {
                    is CatsViewUiData -> {
                        view.populate(it.result)
                    }
                }
            }
        }
        view.setOnFactRequestListener {
            viewModel.refreshPage()
        }

        viewModel.refreshPage()
    }
}
