package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
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

    override fun populate(entity: CatEntity) {
        findViewById<TextView>(R.id.fact_textView).text = entity.catFact.text

        val imageView = findViewById<ImageView>(R.id.cat_image)
        Picasso.get().load(entity.catImageUrl.url).into(imageView)
    }

    override fun showError(message: String?) {
        val msg = message ?: context.getString(R.string.unknown_error)

        showToast(msg)
    }

    override fun showError(messageId: Int?) {
        val msg = messageId?.let {
            context.getString(it)
        } ?: context.getString(R.string.unknown_error)

        showToast(msg)
    }

    private fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(entity: CatEntity)

    fun showError(message: String? = null)

    fun showError(messageId: Int? = null)
}
