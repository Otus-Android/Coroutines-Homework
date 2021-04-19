package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter :CatsPresenter? = null
    private var swipeRefresh: SwipeRefreshLayout? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
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
            Toast.makeText(this.context, fact.image, Toast.LENGTH_LONG).show()
        }
    }

    override fun showErrorDialog(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
    }

    override fun stopRefreshing() {
        swipeRefresh?.isRefreshing = false
    }
}

interface ICatsView {

    fun populate(fact: Fact)
    fun showErrorDialog(message: String)
    fun stopRefreshing()
}