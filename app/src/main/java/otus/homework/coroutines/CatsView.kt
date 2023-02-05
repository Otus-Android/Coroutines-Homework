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
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(catInfo: CatInfo) {
        findViewById<TextView>(R.id.fact_textView).text = catInfo.text
        Picasso.get().load(catInfo.imageUrl).into(findViewById<ImageView>(R.id.cat_image_view));
    }

    override fun showMessage(text: String?) {
        Toast.makeText(
            context, text ?: context.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
    }

    override fun showConnectionErrorMessage() {
        Toast.makeText(context,
            context.getString(R.string.server_connection_error),
            Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(catInfo: CatInfo)
    fun showMessage(text: String?)
    fun showConnectionErrorMessage()
}