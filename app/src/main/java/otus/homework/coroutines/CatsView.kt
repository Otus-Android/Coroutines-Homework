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

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(model: CatsUiModel) {
        findViewById<TextView>(R.id.fact_textView).text = model.fact
        Picasso.get().load(model.pictureUrl).into(findViewById<ImageView>(R.id.pic_imageView))
    }

    override fun showError(error: Throwable) {
        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun showNetworkError() {
        Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(model: CatsUiModel)
    fun showError(error: Throwable)
    fun showNetworkError()
}