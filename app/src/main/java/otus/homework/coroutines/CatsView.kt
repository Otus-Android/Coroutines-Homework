package otus.homework.coroutines

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.dto.Fact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null

    private lateinit var catTextView: TextView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
        catTextView = findViewById(R.id.fact_textView)
        catTextView.movementMethod = ScrollingMovementMethod()
    }

    override fun populateFact(fact: Fact) {
        catTextView.text = fact.text
    }

    override fun showToast(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    override fun populateImage(imageUrl: String) {
        Picasso.get().load(imageUrl).fit().into(findViewById<ImageView>(R.id.cat_imageView))
    }
}

interface ICatsView {
    fun populateFact(fact: Fact)
    fun populateImage(imageUrl: String)
    fun showToast(message: String)
}