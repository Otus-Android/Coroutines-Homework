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

    var presenter :CatsPresenter? = null
    private val picasso by lazy { Picasso.get() }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(model: CatViewModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        model.pictureUrl.let { url ->
            picasso
                .load(url)
                .into(findViewById<ImageView>(R.id.picture_imageView))
        }
    }

    override fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {
    fun populate(model: CatViewModel)
    fun showToast(text: String)
}