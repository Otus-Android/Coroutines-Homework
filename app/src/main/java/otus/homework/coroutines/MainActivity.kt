package otus.homework.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import otus.homework.coroutines.entity.Animal
import otus.homework.coroutines.entity.Result
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val viewModel: MainViewModel = diContainer.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        findViewById<Button>(R.id.button).setOnClickListener { viewModel.initState() }

        viewModel.result.observe(this) {
            when (it) {
                is Result.Success -> applyContent(it.animal)
                is Result.Error -> showErrorMessage(it.throwable)
            }
        }
    }

    private fun applyContent(animal: Animal) {
        findViewById<TextView>(R.id.fact_textView).text = animal.text

        val imageView = findViewById<ImageView>(R.id.cat_image)
        Picasso.get()
            .load(animal.images)
            .placeholder(R.drawable.ic_error)
            .error(R.drawable.ic_error)
            .into(imageView)
    }

    private fun showErrorMessage(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException -> {
                showToast(getString(R.string.error_socket_text))
            }
            else -> {
                showToast(throwable.message)
            }
        }
    }

    private fun showToast(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}