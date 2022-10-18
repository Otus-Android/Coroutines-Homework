package otus.homework.coroutines

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity(), ICatsView {

    private val diContainer = DiContainer()

    private val viewModel by viewModels<MainViewModel> {
        MainViewModelFactory(diContainer.service)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launchWhenStarted {
            viewModel.result.collect { result ->
                when (result) {
                    Result.NoResult -> getData()
                    is Result.Success -> populate(result.bundle)
                    is Result.Error -> showToast(result.error)
                }
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            getData()
        }
    }

    private fun getData() {
        viewModel.getCatsInformation()
    }

    override fun populate(bundle: Bundle) {

        findViewById<TextView>(R.id.fact_textView).text = bundle.getString("fact")
        val imageUrl = bundle.getString("image")!!
        Picasso.get()
            .load(imageUrl)
            .into(findViewById<ImageView>(R.id.imageCats))

    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
