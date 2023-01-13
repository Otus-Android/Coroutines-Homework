package otus.homework.coroutines

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        view.setButtonClickListener { viewModel.onInitComplete() }
        setContentView(view)

        viewModel.onInitComplete()

        viewModel.viewObject.observe(this) { result ->
            when (result) {
                is ResultOf.Success -> view.populate(result.value)
                // TODO: Знаю, что ивент может случиться дважды. В рамках данной программы оставил как есть
                is ResultOf.Failure -> view.showToast(result.message)
            }
        }
    }

}