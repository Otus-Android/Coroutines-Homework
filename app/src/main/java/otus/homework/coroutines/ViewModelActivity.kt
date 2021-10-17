package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class ViewModelActivity : AppCompatActivity() {
    private val viewModel by viewModels<CatViewModel> { CatViewModelFactory(DiContainer.service) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Переиспользуем View чтобы не плодить лишний код
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        val requestButton = findViewById<Button>(R.id.button)

        requestButton.setOnClickListener {
            viewModel.requestFact()
        }

        viewModel.state.observe(this) { state ->
            when (state) {
                is Result.Success -> view.populate(state.data)
                is Result.Error -> view.showMessage(state.message)
            }
        }
    }
}