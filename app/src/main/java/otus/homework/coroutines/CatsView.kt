package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var viewModel: CatsViewModel? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.onInitComplete()
        }
        imageView = findViewById(R.id.cats_image)
        textView = findViewById(R.id.fact_textView)
    }

    override fun populate(fact: Fact) {
        textView.text = fact.text
    }

    override fun populateImage(image: CatImage) {
        Picasso.get()
            .load(image.file)
            .into(imageView)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun populateImage(image: CatImage)
    fun showError(message: String)
}