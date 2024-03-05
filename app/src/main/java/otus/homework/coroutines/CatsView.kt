package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.ImageView
import android.widget.Toast
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

    override fun populate(factWithImage: FactWithImage) {
        findViewById<TextView>(R.id.fact_textView).text = factWithImage.textFact
        Picasso.get().load(factWithImage.imageUrl).into(findViewById<ImageView>(R.id.imageView))
    }

    override fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    override fun showToast(id: Int) {
        val text = context.getString(id)
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

}

interface ICatsView {

    fun populate(factWithImage: FactWithImage)
    fun showToast(text: String)
    fun showToast(id: Int)
}