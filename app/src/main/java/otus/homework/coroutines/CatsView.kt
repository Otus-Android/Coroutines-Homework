package otus.homework.coroutines

import android.content.Context
import android.telecom.Call
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private lateinit var image: ImageView
    private lateinit var text: TextView
    private lateinit var button: Button
    private lateinit var progressBar: ProgressBar

    private val loadingCallback = object : Callback {
        override fun onSuccess() = hideProgress()
        override fun onError(e: Exception?) = hideProgress()
    }

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()

        image = findViewById(R.id.fact_imageView)
        text = findViewById(R.id.fact_textView)
        button = findViewById<Button>(R.id.button).apply {
            setOnClickListener {
                presenter?.onInitComplete()
            }
        }
        progressBar = findViewById(R.id.fact_progressBar)
    }

    override fun populate(data: CatData) {
        text.text = data.fact.text

        showProgress()
        Picasso.get()
            .load(data.image.url)
            .into(image, loadingCallback)
    }

    override fun showProgress() {
        progressBar.isVisible = true
    }

    override fun hideProgress() {
        progressBar.isVisible = false
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun showError(message: String)
    fun populate(data: CatData)
    fun showProgress()
    fun hideProgress()
}