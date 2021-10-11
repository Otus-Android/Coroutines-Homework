package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.api.Fact
import otus.homework.coroutines.utils.showToast

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

    override fun populate(fact: Fact, imageUrl: String) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get().load(imageUrl).into(findViewById<ImageView>(R.id.image))
    }

    override fun showError(message: String) {
        context.showToast(message)
    }

    override fun showNetworkError() {
        context.showToast(R.string.error_text_no_network)
    }
}

interface ICatsView {

    fun populate(fact: Fact, imageUrl: String)

    fun showError(message: String)

    fun showNetworkError()
}