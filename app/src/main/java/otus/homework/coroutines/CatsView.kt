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

    override fun populate(model: CatsModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact.text
        Picasso.get()
            .load(model.image.fileUrl)
            .fit()
            .centerInside()
            .into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showNoResponseToast() {
        showToast(context.getString(R.string.no_server_response))
    }
}

interface ICatsView {
    fun populate(model: CatsModel)
    fun showToast(message: String)
    fun showNoResponseToast()
}