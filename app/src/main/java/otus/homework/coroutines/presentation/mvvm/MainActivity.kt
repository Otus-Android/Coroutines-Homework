package otus.homework.coroutines.presentation.mvvm

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import otus.homework.coroutines.R
import otus.homework.coroutines.di.DiContainer

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer(this)
    private val viewModel: CatsViewModel by viewModels { CatsViewModelFactory(diContainer) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView = findViewById<ImageView>(R.id.fact_img)
        val textView = findViewById<TextView>(R.id.fact_textView)
        val button = findViewById<Button>(R.id.button)

        viewModel.viewStateLiveData.observe(this) {
            it.apply(imageView, textView)
        }

        button.setOnClickListener {
            viewModel.getNewCatsData()
        }

        viewModel.getNewCatsData()
    }
}