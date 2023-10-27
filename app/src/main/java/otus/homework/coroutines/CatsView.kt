package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.CatUi

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private val picasso = Picasso.Builder(context).build()
    private lateinit var catFactTextView: TextView
    private lateinit var catImageImageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
        catFactTextView = findViewById(R.id.fact_textView)
        catImageImageView = findViewById(R.id.cat_image)
    }

    override fun populate(cat: CatUi) {
        catFactTextView.text = cat.fact
        picasso
            .load(cat.imageUrl)
            .into(catImageImageView)
    }

    override fun showError(message: String) {
        Toast
            .makeText(context, message, Toast.LENGTH_SHORT)
            .show()
    }
}

interface ICatsView {

    fun populate(cat: CatUi)
    fun showError(message: String)
}