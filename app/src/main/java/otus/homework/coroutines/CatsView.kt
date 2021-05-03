package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter: CatsPresenter? = null
    private var refreshLayout: SwipeRefreshLayout? = null


    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe)
        refreshLayout?.setOnRefreshListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(factImage: FactImage) {
        refreshLayout?.isRefreshing = false
        findViewById<TextView>(R.id.fact_textView).text = factImage.fact.text
        Picasso.get().load(factImage.image.file).into(findViewById<ImageView>(R.id.iv_image))
    }

    override fun showToast(message: String) {
        Toast.makeText(context, message, LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(factImage: FactImage)
    fun showToast(message: String)
}