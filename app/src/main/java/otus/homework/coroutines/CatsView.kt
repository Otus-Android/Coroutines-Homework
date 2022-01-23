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

    override fun populate(fact: Fact, file: String) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get().load(file)
            .into(findViewById<ImageView>(R.id.cat_image))

    }

    override fun showToastMessage(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact, file: String)
    fun showToastMessage(text: String)
}