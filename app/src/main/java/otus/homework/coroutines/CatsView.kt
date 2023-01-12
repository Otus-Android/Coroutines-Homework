package otus.homework.coroutines

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout

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

    override fun populate(catFact: CatFact) {
        findViewById<TextView>(R.id.fact_textView).text = catFact.fact?.fact
        findViewById<ImageView>(R.id.cat_image).setImageBitmap(catFact.catImage)
    }

    override fun showErrorMessage(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_LONG).show()
        }
    }
}

interface ICatsView {

    fun populate(fact: CatFact)

    fun showErrorMessage(message: String)
}