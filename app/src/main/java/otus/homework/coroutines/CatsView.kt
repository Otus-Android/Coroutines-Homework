package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: CatFactWithImage) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        val url = "https://cataas.com${fact.imageUrl}"
        Picasso.get().load(url).into(
            findViewById<ImageView>(R.id.cat_imageView)
        )
    }

    override fun showError(error: String?) {
        val errorText = error ?: context.getString(R.string.error_unknown)
        Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show()
    }

    override fun showError(@StringRes error: Int) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: CatFactWithImage)

    fun showError(error: String?)
    fun showError(@StringRes error: Int)
}
