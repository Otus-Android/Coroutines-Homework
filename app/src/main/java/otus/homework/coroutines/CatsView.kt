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

    override fun populate(fact: Fact) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        if ( fact.source != "" )
            Picasso.get().load(fact.source)
                .into(findViewById<ImageView>(R.id.fact_imageView))
    }

    override fun toast(str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }
}

interface ICatsView {

    fun populate(fact: Fact)

    fun toast(str: String)
}