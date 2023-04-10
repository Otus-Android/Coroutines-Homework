package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.Display
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import otus.homework.coroutines.model.CatFact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    var imageLoader: ImageLoader? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catFact: CatFact) {
        findViewById<TextView>(R.id.fact_textView).text = catFact.fact
        imageLoader?.load(catFact.imageUrl, findViewById(R.id.cat_imageView))
    }

    override fun showErrorToast(isSocketTimeout: Boolean, message: String?) {
        if (isSocketTimeout) Toast.makeText(
            this.context,
            this.context.getString(R.string.socket_timeout_ex),
            Toast.LENGTH_SHORT
        ).show()
        else Toast.makeText(
            this.context,
            "${this.context.getString(R.string.loading_ex)} ${message ?: ""}",
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {

    fun populate(catFact: CatFact)

    fun showErrorToast(isSocketTimeout: Boolean, message: String? = null)
}