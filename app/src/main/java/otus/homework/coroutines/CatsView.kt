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

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catData: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = catData.fact
        val imageView = findViewById<ImageView>(R.id.fact_imageView)
        Picasso.get()
            .load(catData.picUrl)
            .into(imageView)
    }

    override fun showSocketTimeoutExceptionToast() {
        Toast.makeText(
            context,
            R.string.socket_timeout_exception_message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showDefaultExceptionToast(exceptionMessage: String?) {
        Toast.makeText(
            context,
            exceptionMessage ?: context.getString(R.string.default_exception_message),
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {

    fun populate(catData: CatData)

    fun showSocketTimeoutExceptionToast()

    fun showDefaultExceptionToast(exceptionMessage: String?)
}

data class CatData(
    val fact: String?,
    val picUrl: String?
)