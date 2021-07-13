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

class MainActivity : AppCompatActivity() {

    lateinit var catsPresenter: CatsPresenter
    private val diContainer = DiContainer()
    private var _catsView: ICatsView? = null
    private val viewModel: MainViewModel = diContainer.viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)

        // old logic
//        catsPresenter = CatsPresenter(
//            catsService = diContainer.service,
//            imageService = diContainer.imageService
//        )
//        view.presenter = catsPresenter
//        catsPresenter.attachView(view)
//        catsPresenter.onInitComplete()
//        catsPresenter.error.observe(this) {
//            when (it) {
//                is ErrorState.SocketError -> showToast(getString(R.string.error_socket_text))
//                is ErrorState.OtherError -> showToast(it.message)
//            }
//        }

        findViewById<Button>(R.id.button).setOnClickListener { viewModel.getContent() }

        viewModel.result.observe(this) {
            when (it) {
                is Result.Success -> {
                    applyContent(it.animal)
                }
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

    override fun onStop() {
        if (isFinishing) {
            catsPresenter.detachView()
        }
        super.onStop()
    }

    private fun showToast(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}