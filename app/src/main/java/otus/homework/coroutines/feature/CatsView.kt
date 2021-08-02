package otus.homework.coroutines.feature

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.model.CatsData

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null
    private lateinit var imageView: ImageView

    override fun onFinishInflate() {
        super.onFinishInflate()

        imageView = findViewById(R.id.imageView)

        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catsData: CatsData) {
        findViewById<TextView>(R.id.fact_textView).text = catsData.text
        if (catsData.uri.isNotEmpty()) {
            Picasso.get().load(catsData.uri).into(imageView)
        }
    }

    override fun toastSocketTimeoutException(throwable: Throwable) {
        Toast.makeText(context, context.getString(R.string.socket_timeout_exception) + ": " + throwable.message, LENGTH_LONG).show()
    }

    override fun toastSomeException(throwable: Throwable) {
        Toast.makeText(context, context.getString(R.string.some_exception) + ": " + throwable.message, LENGTH_LONG).show()
    }

}

interface ICatsView {

    fun populate(catsData: CatsData)
    fun toastSocketTimeoutException(throwable: Throwable)
    fun toastSomeException(throwable: Throwable)
}