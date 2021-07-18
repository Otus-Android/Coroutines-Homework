package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: CatsViewModel by viewModels()
        viewModel.diContainer = diContainer
        viewModel.onInitComplete()
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        viewModel.getResult().observe(this, {
            when(it){
                is Result.Error -> {
                    val message = it.message ?: it.messageRes?.let { str -> getString(str) }
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
                is Result.Success -> view.populate(it.factWithPicture)
            }
        })

        view.setOnClickListener {
            viewModel.onInitComplete()
        }
    }
}