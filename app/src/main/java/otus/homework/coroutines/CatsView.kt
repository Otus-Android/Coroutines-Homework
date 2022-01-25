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

    var presenter :CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: IllustratedFact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact.text
        Picasso.get().load(fact.image.filePath).into(findViewById<ImageView>(R.id.fact_imageView))
    }

    override fun showResourceString(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_SHORT).show()
    }

    override fun showErrorText(error: String?) {
        if (error == null)
            showResourceString(R.string.unknown_error)
        else
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: IllustratedFact)
    fun showResourceString(stringId: Int)
    fun showErrorText(error: String?)
}