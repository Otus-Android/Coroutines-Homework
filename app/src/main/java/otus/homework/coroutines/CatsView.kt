package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import java.net.SocketTimeoutException

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var loadAction: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            loadAction?.invoke()
        }
    }

    override fun populate(catPresentation: CatPresentation) {
        findViewById<TextView>(R.id.fact_textView).text = catPresentation.fact

        if (catPresentation.imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(catPresentation.imageUrl)
                .into(findViewById<ImageView>(R.id.cat_imageView))
        }
    }

    override fun showError(message: Exception) {
        val text = when (message) {
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            else -> message.message ?: context.getString(R.string.error_unknown)
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}

interface ICatsView {

    fun populate(catPresentation: CatPresentation)
    fun showError(message: Exception)
}