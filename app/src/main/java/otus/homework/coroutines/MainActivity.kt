package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val viewModel: CatsViewModel by viewModels { CatsViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        view.viewModel = viewModel
        viewModel.onInitComplete()
        viewModel._catsData.observe(this) {
            when (it) {
                is Result.Success -> view.populate(it.item)
                else -> {
                    Toast.makeText(
                        this,
                        "Не удалось получить ответ от сервера",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}