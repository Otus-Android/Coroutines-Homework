package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
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

    var presenter :CatsPresenter? = null
    var catViewModel: CatsViewModel? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            if (Constants.PRESENTER_VARIANT) {
                presenter?.onInitComplete()
            } else {
                catViewModel?.updateCat()
            }
        }
    }

    override fun populate(catDescription: CatDescription) {
        findViewById<TextView>(R.id.fact_textView).text = catDescription.fact
        val image = findViewById<ImageView>(R.id.cat_image)
        Picasso.get()
            .load(catDescription.imageUrl)
            .placeholder(R.drawable.loading_img)
            .error(R.drawable.ic_broken_image)
            .into(image)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(@StringRes messageId: Int, vararg formatArgs: Any?) {
        showError(context.getString(messageId, formatArgs))
    }

    override fun setLoading(loading: Boolean) {
        findViewById<Button>(R.id.button).visibility = if (loading) View.GONE else View.VISIBLE
        findViewById<ProgressBar>(R.id.fact_loading).visibility = if (loading) View.VISIBLE else View.GONE
    }
}

interface ICatsView {

    fun populate(catDescription: CatDescription)

    fun showError(message: String)
    fun showError(@StringRes messageId: Int, vararg formatArgs: Any?)

    fun setLoading(loading: Boolean)
}