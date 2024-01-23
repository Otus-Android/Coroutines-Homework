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

    var presenter :CatsPresenter? = null
    var model: CatViewModel? = null
    var NetworkTimeoutString = resources.getString(R.string.net_timeout)

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            //presenter?.onInitComplete()
            model?.updateData()
        }
    }

    override fun populate(model: Model) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        val imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(model.imageUrl).into(imageView)
    }

    override fun showError(e: Exception) {
        val text = if (e is SocketTimeoutException) {
            NetworkTimeoutString
        } else {
            e.message?:""
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(model: Model)
    fun showError(e: Exception)
}