package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
            presenter?.onInitComplete(context)
        }
    }



    override fun populate(fact: Fact, picture: Picture) {
        findViewById<TextView>(R.id.fact_textView).text = fact.text
        Picasso.get().load(picture.file).into(findViewById<ImageView>(R.id.iv_for_cat_picture))
    }
}

interface ICatsView {

    fun populate(fact: Fact, picture: Picture)
}