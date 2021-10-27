package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.lang.Exception

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact, randomImage: RandomImage) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text

        val imageView = findViewById<ImageView>(R.id.fact_imageView)
        Picasso.get()
            .load(randomImage.imageUrl)
            .into(imageView)
    }

    override fun showTimeoutError() {
        Toast.makeText(context, R.string.timeout_error, Toast.LENGTH_SHORT).show()
    }

    override fun showError(exception: Exception) {
        Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact, randomImage: RandomImage)

    fun showTimeoutError()

    fun showError(exception: Exception)
}