package otus.homework.coroutines

import android.content.Context
import android.net.Uri
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
            .load(Uri.parse(cat.image.url))
            .into(findViewById<ImageView>(R.id.image))
    }

    override fun showNoConnectivityMessage() {
        Toast.makeText(context, R.string.no_connectivity_message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(cat: Cat)

    fun showNoConnectivityMessage()
}