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

    var presenter: CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(data: CatData) {
        findViewById<TextView>(R.id.fact_textView).text = data.fact.text
        Picasso.get()
            .load(data.image.url)
            .into(findViewById<ImageView>(R.id.image))
    }

    override fun error(message: String) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(data: CatData)

    fun error(message: String)
}