package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.models.Cat

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

    override fun populate(cat: Cat) {
        findViewById<TextView>(R.id.fact_textView).text = cat.fact.text
        Picasso.get()
            .load(cat.catPhoto.photoUrl)
            .into(findViewById<ImageView>(R.id.photo_imageView))
    }

    override fun showError(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showServerResponseError() {
        Toast.makeText(
            context,
            R.string.socket_timeout_exception_toast,
            Toast.LENGTH_SHORT
        ).show()
    }
}

interface ICatsView {

    fun populate(cat: Cat)
    fun showError(message: String)
    fun showServerResponseError()
}