package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private lateinit var factTextView: TextView
    private lateinit var catImageView: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var button: android.widget.Button

    override fun onFinishInflate() {
        super.onFinishInflate()
        factTextView = findViewById(R.id.fact_textView)
        catImageView = findViewById(R.id.cat_imageView)
        progressBar = findViewById(R.id.progress_bar)
        button = findViewById(R.id.button)
    }

    override fun populate(data: CatPresentationData) {
        showContent()

        factTextView.text = data.fact

        Picasso.get()
            .load(data.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(catImageView)
    }

    override fun showError(message: String) {
        showContent()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressBar.visibility = VISIBLE
        catImageView.visibility = GONE
        factTextView.visibility = GONE
        button.isEnabled = false
    }

    private fun showContent() {
        progressBar.visibility = GONE
        catImageView.visibility = VISIBLE
        factTextView.visibility = VISIBLE
        button.isEnabled = true
    }
}