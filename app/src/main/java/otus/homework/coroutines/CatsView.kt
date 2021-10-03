package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private var swipeRefresh: SwipeRefreshLayout? = null
    private var factButton: Button? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        factButton = findViewById<Button>(R.id.button)
        factButton?.setOnClickListener {
            presenter?.onInitComplete()
        }
        swipeRefresh = findViewById(R.id.refresh_layout)
        swipeRefresh?.setOnRefreshListener {
            swipeRefresh?.isRefreshing = true
            presenter?.loadFactAndImage()
        }
    }

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        if (fact.image.isNotEmpty()) {
            Picasso.get().load(fact.image).into(
                findViewById<ImageView>(R.id.cat_image_view)
            )
        }
    }

    override fun showErrorDialog(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    override fun stopRefreshing() {
        swipeRefresh?.isRefreshing = false
    }

    override fun onLoading(value: Boolean) {
        factButton?.isEnabled = !value
        swipeRefresh?.isEnabled = !value
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun showErrorDialog(message: String)
    fun stopRefreshing()
    fun onLoading(value: Boolean)
}