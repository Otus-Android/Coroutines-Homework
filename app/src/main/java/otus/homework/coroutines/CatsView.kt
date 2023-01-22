package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private val refresher by lazy { findViewById<SwipeRefreshLayout>(R.id.swipeRefresh) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progressBar) }
    override fun onFinishInflate() {
        super.onFinishInflate()
        refresher.setOnRefreshListener {
            progressBar.visibility = View.VISIBLE
            presenter?.onInitComplete()
        }
    }

    override fun populate(item: CatItem) {
        findViewById<TextView>(R.id.fact_textView).text = item.fact

        Picasso.get()
            .load(item.url)
            .into(
                findViewById(R.id.imageView),
                object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        progressBar.visibility = View.GONE
                        message(e.toString())
                    }
                }
            )
        refresher.isRefreshing = false
    }

    override fun message(resId: Int) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun message(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(item: CatItem)

    fun message(resId: Int)
    fun message(text: String)
}
