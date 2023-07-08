package otus.homework.coroutines.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.squareup.picasso.Picasso
import otus.homework.coroutines.R
import otus.homework.coroutines.network.models.CatsImage
import otus.homework.coroutines.network.models.Fact

class CatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    var presenter : CatsPresenter? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            presenter?.onInitComplete()
        }
    }

    override fun populate(fact: Fact, catsImage: CatsImage) {
        findViewById<TextView>(R.id.fact_textView).text = fact.fact
        Picasso.get().load(catsImage.catsUrl).into(findViewById<ImageView>(R.id.imageView))
    }
}

interface ICatsView {

    fun populate(fact: Fact,catsImage:CatsImage)
}