package otus.homework.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        viewModel.onInitComplete()
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        setListeners(view)
    }

    private fun setListeners(view: CatsView) {
        viewModel.getResult().observe(this, {
            when (it) {
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

    private fun initViewModel() {
        val viewModel: CatsViewModel by viewModels { ViewModelFactory(DiContainer()) }
        this.viewModel = viewModel
    }
}